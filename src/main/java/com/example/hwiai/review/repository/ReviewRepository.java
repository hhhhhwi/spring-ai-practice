package com.example.hwiai.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hwiai.review.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductIdAndIsAggregated(Long productId, boolean isAggregated);
}