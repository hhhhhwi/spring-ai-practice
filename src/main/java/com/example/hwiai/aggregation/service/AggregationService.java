package com.example.hwiai.aggregation.service;

import org.springframework.stereotype.Service;

import com.example.hwiai.evaluation.Evaluation;
import com.example.hwiai.evaluation.service.EvaluationService;

@Service
public class AggregationService {
    private final EvaluationService evaluationService;
    
    public AggregationService(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    public int aggregateScores(Long productId, Long characteristicId) {
        return (int) Math.round(
            evaluationService
                .findByReviewProductIdAndCharacteristicIdAndIsRelatedTrue(productId, characteristicId)
                .stream()
                .mapToInt(Evaluation::getScoreValue)
                .average()
                .orElse(0.0)
            );
    }
}
