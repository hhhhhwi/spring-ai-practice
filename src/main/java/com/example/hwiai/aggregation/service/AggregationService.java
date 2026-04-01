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

    public String aggregateChoices(Long productId, Long characteristicId) {
        return evaluationService
                .findByReviewProductIdAndCharacteristicIdAndIsRelatedTrue(productId, characteristicId)
                .stream()
                .map(Evaluation::getStringValue)
                .collect(Collectors.groupingBy(value -> value, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");
    }
}
