package com.example.hwiai.analyzer;

import org.springframework.ai.chat.client.ChatClient;

import com.example.hwiai.analyzer.dto.AnalyzedResponse;
import com.example.hwiai.characteristic.Characteristic;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ScoreBasedAnalyzer extends BaseAnalyzer {
    public ScoreBasedAnalyzer(Characteristic characteristic, ChatClient.Builder chatClientBuilder
        , ObjectMapper objectMapper) {
        super(characteristic, chatClientBuilder, objectMapper);
    }

    @Override
    public abstract String getPrompt();

    @Override
    public boolean validate(AnalyzedResponse response) {
        if(response.isRelated()) {
            return response.getScoreValue() > 0;
        }

        return response.getScoreValue() == 0;
    }
}
