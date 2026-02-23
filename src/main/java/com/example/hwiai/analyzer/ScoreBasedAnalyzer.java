package com.example.hwiai.analyzer;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import com.example.hwiai.analyzer.dto.AnalyzedResponse;
import com.example.hwiai.characteristic.ValueType;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ScoreBasedAnalyzer extends BaseAnalyzer {
    public ScoreBasedAnalyzer(ChatClient.Builder chatClientBuilder, ObjectMapper objectMapper) {
        super(chatClientBuilder, objectMapper);
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
