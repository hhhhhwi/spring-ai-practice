package com.example.hwiai.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hwiai.review.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select r from Review r where r.product.id = :productId and r.text is not null")
    List<Review> findReviewsWithTextByProductId(@Param("productId") Long productId);

    default List<Review> findAnalyzableByProductId(Long productId) {
        return findReviewsWithTextByProductId(productId).stream()
                .filter(review -> !review.getText().isBlank())
                .toList();
    }

    List<Review> findByProductId(Long productId);
}
