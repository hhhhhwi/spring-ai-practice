package com.example.hwiai.review.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.hwiai.product.Product;
import com.example.hwiai.product.repository.ProductRepository;
import com.example.hwiai.review.Review;
import com.example.hwiai.review.dto.ReviewRequest;
import com.example.hwiai.review.repository.ReviewRepository;

@Service
public class ReviewService {
    private ReviewRepository reviewRepository;
    private ProductRepository productRepository;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    public List<Review> findByProductIdAndTextIsNotNull(Long productId) {
        List<Review> reviews = reviewRepository.findByProductIdAndTextIsNotNull(productId);

        if (reviews.size() == 0) {
            throw new RuntimeException("No reviews with text found for product ID: " + productId);
        }

        return reviews;
    }

    public List<Review> findByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    public Review saveReview(ReviewRequest request) {
        Long productId = request.getProductId();
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        Review review = new Review(product, request.getText());
        return reviewRepository.save(review);
    }

    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId)
        .orElseThrow(() -> new RuntimeException("Product not found with ID: " + reviewId)); // TODO customeException 생성
    }
}
