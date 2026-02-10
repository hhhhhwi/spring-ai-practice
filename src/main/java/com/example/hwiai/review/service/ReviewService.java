package com.example.hwiai.review.service;

import org.springframework.stereotype.Service;

import com.example.hwiai.evaluation.Evaluation;
import com.example.hwiai.evaluation.repository.EvaluationRepository;
import com.example.hwiai.product.Product;
import com.example.hwiai.product.repository.ProductRepository;
import com.example.hwiai.review.Review;
import com.example.hwiai.review.dto.ReviewRequest;
import com.example.hwiai.review.repository.ReviewRepository;

@Service
public class ReviewService {
    private ReviewRepository reviewRepository;
    private ProductRepository productRepository;
    private EvaluationRepository characteristicValueRepository;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository,
            EvaluationRepository characteristicValueRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.characteristicValueRepository = characteristicValueRepository;
    }


    public void saveReview(ReviewRequest request) {
        Long productId = request.getProductId();
        int scoreValue = request.getScoreValue();
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        Review review = new Review(product, request.getText());
        reviewRepository.save(review);

        Evaluation characteristicValue = new Evaluation(review, scoreValue);
        characteristicValueRepository.save(characteristicValue);
    }
}
