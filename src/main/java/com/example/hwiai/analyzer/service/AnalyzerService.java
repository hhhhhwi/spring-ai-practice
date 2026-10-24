package com.example.hwiai.analyzer.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.hwiai.analyzer.AnalyzerRegistry;
import com.example.hwiai.analyzer.dto.AnalyzedResponse;
import com.example.hwiai.characteristic.Characteristic;
import com.example.hwiai.characteristic.ValueType;
import com.example.hwiai.characteristic.service.CharacteristicService;
import com.example.hwiai.error.exception.AnalysisConfigurationException;
import com.example.hwiai.error.exception.AnalysisPreconditionException;
import com.example.hwiai.error.exception.ProductNotFoundException;
import com.example.hwiai.error.exception.UnexpectedAnalyzerResponseException;
import com.example.hwiai.evaluation.Evaluation;
import com.example.hwiai.evaluation.service.EvaluationService;
import com.example.hwiai.product.repository.ProductRepository;
import com.example.hwiai.review.Review;
import com.example.hwiai.review.service.ReviewService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AnalyzerService {
    private final ReviewService reviewService;
    private final CharacteristicService characteristicService;
    private final EvaluationService evaluationService;
    private final AnalyzerRegistry analyzerRegistry;
    private final ProductRepository productRepository;

    public AnalyzerService(ReviewService reviewService, CharacteristicService characteristicService,
            EvaluationService evaluationService, AnalyzerRegistry analyzerRegistry,
            ProductRepository productRepository) {
        this.reviewService = reviewService;
        this.characteristicService = characteristicService;
        this.evaluationService = evaluationService;
        this.analyzerRegistry = analyzerRegistry;
        this.productRepository = productRepository;
    }

    @Transactional
    public void analyze(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }

        List<Review> reviews = reviewService.findAnalyzableByProductId(productId);
        if (reviews.isEmpty()) {
            throw new AnalysisPreconditionException("No analyzable reviews found for productId=" + productId);
        }

        List<Characteristic> characteristics = characteristicService.findByIsActiveTrue();
        if (characteristics.isEmpty()) {
            throw new AnalysisPreconditionException("No active characteristics found");
        }

        Map<Long, Review> reviewById = reviews.stream()
        .collect(Collectors.toMap(Review::getId, review -> review));

        List<Evaluation> allEvaluations = new ArrayList<>();

        for (Characteristic characteristic : characteristics) {
            List<Review> targetReviews = findReviewsWithoutEvaluation(reviews, characteristic.getId());
            if (targetReviews.isEmpty()) {
                log.info("No analysis target. productId={}, characteristicId={}", productId, characteristic.getId());
                continue;
            }
            Set<Long> requestedReviewIds = targetReviews.stream()
                    .map(Review::getId)
                    .collect(Collectors.toCollection(HashSet::new));

            analyzeByCharacList(characteristic, targetReviews)
                .forEach(response -> {
                    if (response.getReviewId() == null || !requestedReviewIds.contains(response.getReviewId())) {
                        throw new UnexpectedAnalyzerResponseException(response.getReviewId(), characteristic.getId());
                    }

                    if(evaluationService.existsByReviewIdAndCharacteristicId(response.getReviewId(), characteristic.getId())) {
                        log.info("Evaluation already exists. reviewId={}, characteristicId={}", response.getReviewId(), characteristic.getId());
                        return;
                    }

                    Review review = reviewById.get(response.getReviewId());
                    Evaluation evaluation = createEvaluation(review, characteristic, response);
                    allEvaluations.add(evaluation);
                });
        }

        evaluationService.saveAll(allEvaluations);
    }

    public List<AnalyzedResponse> analyzeByCharacList(Characteristic characteristic, List<Review> reviews) {
        try {
            return analyzerRegistry.getAnalyzer(characteristic.getValueType())
                    .analyze(characteristic, reviews);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new AnalysisConfigurationException(
                    "Unsupported valueType=" + characteristic.getValueType()
                            + " for characteristicId=" + characteristic.getId());
        }
    }

    private Evaluation createEvaluation(Review review, Characteristic characteristic, AnalyzedResponse response) {
        if (characteristic.getValueType() == ValueType.CHOICE) {
            return new Evaluation(
                    review,
                    response.isRelated(),
                    characteristic,
                    response.getChoiceValue(),
                    response.getPhrase());
        }

        if (characteristic.getValueType() == ValueType.SCORE) {
            return new Evaluation(
                review,
                response.isRelated(),
                characteristic,
                response.getScoreValue(),
                response.getPhrase());
        }

        throw new AnalysisConfigurationException(
                "Unsupported valueType=" + characteristic.getValueType()
                        + " for characteristicId=" + characteristic.getId());
    }

    private List<Review> findReviewsWithoutEvaluation(List<Review> reviews, Long characteristicId) {
        return reviews.stream()
                .filter(review -> !evaluationService.existsByReviewIdAndCharacteristicId(review.getId(), characteristicId))
                .toList();
    }
}
