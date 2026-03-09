package com.example.hwiai.analyzer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.hwiai.analyzer.AnalyzerRegistry;
import com.example.hwiai.analyzer.dto.AnalyzedResponse;
import com.example.hwiai.characteristic.Characteristic;
import com.example.hwiai.characteristic.service.CharacteristicService;
import com.example.hwiai.evaluation.Evaluation;
import com.example.hwiai.evaluation.service.EvaluationService;
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

    public AnalyzerService(ReviewService reviewService, CharacteristicService characteristicService,
            EvaluationService evaluationService, AnalyzerRegistry analyzerRegistry) {
        this.reviewService = reviewService;
        this.characteristicService = characteristicService;
        this.evaluationService = evaluationService;
        this.analyzerRegistry = analyzerRegistry;
    }

    @Transactional
    public void analyze(Long productId) {
        // 분석할 리뷰들 가져오기, 검증
        List<Review> reviews = reviewService.findByProductIdAndTextIsNotNull(productId);

        Map<Long, Review> reviewById = reviews.stream()
        .collect(Collectors.toMap(Review::getId, review -> review));

        List<Evaluation> allEvaluations = new ArrayList<>();

        List<Characteristic> characteristics = characteristicService.findByIsActiveTrue();

        // 분석
        for (Characteristic characteristic : characteristics) {
            analyzeByCharacList(characteristic, reviews)
                .forEach(response -> {
                    // 해당 Characteristic에 해당하는 Evaluation이 있는 지 확인 -> 있으면 로그 남기고 넘어감
                    if(evaluationService.existsByReviewIdAndCharacteristicId(response.getReviewId(), characteristic.getId())) {
                        log.info("이미 평가가 존재합니다. reviewId={}, characteristicId={}", response.getReviewId(), characteristic.getId());
                        return;
                    }

                    Review review = reviewById.get(response.getReviewId());
                    if (review == null) {
                        throw new IllegalStateException(
                            "Analyzer returned unknown reviewId=" + response.getReviewId() + " for productId=" + productId
                        );
                    }
                    Evaluation evaluation = new Evaluation(review, response.isRelated(), characteristic, response.getScoreValue(), response.getPhrase());
                    allEvaluations.add(evaluation);
                });
        }

        // 분석한 내용 저장
        evaluationService.saveAll(allEvaluations);
    }

    public List<AnalyzedResponse> analyzeByCharacList(Characteristic characteristic, List<Review> reviews) {
        return analyzerRegistry.getAnalyzer(characteristic.getValueType())
                .analyze(characteristic, reviews);
    }
}
