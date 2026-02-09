package com.example.hwiai.evaluation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByReviewProductId(Long productId);

}
