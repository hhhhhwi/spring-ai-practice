package com.example.hwiai.review;

import org.springframework.stereotype.Service;

import com.example.hwiai.characteristic.CharacteristicValue;
import com.example.hwiai.characteristic.CharacteristicValueRepository;
import com.example.hwiai.product.Product;
import com.example.hwiai.product.ProductRepository;
import com.example.hwiai.review.dto.ReviewRequest;

@Service
public class ReviewService {
    private ReviewRepository reviewRepository;
    private ProductRepository productRepository;
    private CharacteristicValueRepository characteristicValueRepository;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository,
            CharacteristicValueRepository characteristicValueRepository) {
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

        CharacteristicValue characteristicValue = new CharacteristicValue(review, scoreValue);
        characteristicValueRepository.save(characteristicValue);
    }
}
