package com.example.hwiai.review;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.hwiai.product.Product;
import com.example.hwiai.product.ProductRepository;
import com.example.hwiai.review.dto.ReviewRequest;
import com.example.hwiai.summary.CharacteristicSummary;
import com.example.hwiai.summary.CharacteristicSummaryRepository;

@Service
public class ReviewService {
    private ReviewRepository reviewRepository;
    private ProductRepository productRepository;
    private CharacteristicSummaryRepository characteristicSummaryRepository;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository,
            CharacteristicSummaryRepository characteristicSummaryRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.characteristicSummaryRepository = characteristicSummaryRepository;
    }


    public void saveReview(ReviewRequest request) {
        Long productId = request.getProductId();
        int scoreValue = request.getScoreValue();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        Review review = new Review(product, request.getText(), scoreValue);
        reviewRepository.save(review);

        Optional<CharacteristicSummary> latestSummary = characteristicSummaryRepository.findLatestByProductId(productId);

        if (!latestSummary.isPresent()) {
          CharacteristicSummary summary = new CharacteristicSummary(product, scoreValue, 1);
          characteristicSummaryRepository.save(summary);
          return;
        }

        CharacteristicSummary summary = latestSummary.get();
        summary.addScore(scoreValue);
        characteristicSummaryRepository.save(summary);
    }
}
