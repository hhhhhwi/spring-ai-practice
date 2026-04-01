package com.example.hwiai.aggregation.service;

import com.example.hwiai.characteristic.Characteristic;
import com.example.hwiai.characteristic.service.CharacteristicService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.hwiai.aggregation.dto.AggregationResponse;
import com.example.hwiai.evaluation.Evaluation;
import com.example.hwiai.evaluation.service.EvaluationService;

@Service
public class AggregationService {
    private final EvaluationService evaluationService;
    
    private final CharacteristicService characteristicService;
    
    public AggregationService(EvaluationService evaluationService, CharacteristicService characteristicService) {
        this.evaluationService = evaluationService;
        this.characteristicService = characteristicService;
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

    public List<AggregationResponse> aggregateAll(Long productId) {
        List<Characteristic> characteritics = characteristicService.findByIsActiveTrue();

        return characteritics.stream().map(charac -> {
            String choice = aggregateChoices(productId, charac.getId());
            int score = aggregateScores(productId, charac.getId());
            return new AggregationResponse(charac.getId(), charac.getName(), score, choice);
        }).collect(Collectors.toList());
    }
}
