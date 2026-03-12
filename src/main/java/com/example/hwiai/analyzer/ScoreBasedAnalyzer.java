package com.example.hwiai.analyzer;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.hwiai.analyzer.dto.AnalyzedResponse;
import com.example.hwiai.characteristic.Characteristic;
import com.example.hwiai.characteristic.ValueType;
import com.example.hwiai.review.Review;

@Component
public class ScoreBasedAnalyzer implements CharacteristicAnalyzer {
    private final AnalyzerExecutor analyzerExecutor;

    public ScoreBasedAnalyzer(AnalyzerExecutor analyzerExecutor) {
        this.analyzerExecutor = analyzerExecutor;
    }

    @Override
    public List<AnalyzedResponse> analyze(Characteristic characteristic, List<Review> reviews) {
        return analyzerExecutor.execute(
                characteristic.getPrompt(),
                Map.of(),
                reviews,
                this::validate);
    }

    @Override
    public boolean validate(AnalyzedResponse response) {
        if (response.isRelated()) {
            return response.getScoreValue() > 0;
        }
        return response.getScoreValue() == 0;
    }

    @Override
    public boolean supports(ValueType valueType) {
        return valueType.equals(ValueType.SCORE);
    }
}
