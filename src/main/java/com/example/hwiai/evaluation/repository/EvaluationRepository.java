package com.example.hwiai.evaluation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hwiai.evaluation.Evaluation;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByReviewProductId(Long productId);

    boolean existsByReviewIdAndCharacteristicId(Long reviewId, Long characteristicId);

    List<Evaluation> findByReviewProductIdAndCharacteristicIdAndIsRelatedTrue(Long productId, Long characteristicId);
    
    List<Evaluation> findByReviewId(Long reviewId);
}
