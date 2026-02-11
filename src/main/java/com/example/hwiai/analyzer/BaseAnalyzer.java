package com.example.hwiai.analyzer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;

import com.example.hwiai.analyzer.dto.AnalyzedResponse;
import com.example.hwiai.characteristic.Characteristic;
import com.example.hwiai.review.Review;
import com.example.hwiai.util.AnalyzeValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseAnalyzer implements CharacteristicAnalyzer {
    private final Characteristic characteristic;
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    protected BaseAnalyzer(Characteristic characteristic, ChatClient.Builder chatClientBuilder
        , ObjectMapper objectMapper) {
        this.characteristic = characteristic;
        this.chatClient = chatClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    @Override
    public Characteristic getCharacteristic() {
        return characteristic;
    }

    @Override
    public abstract String getPrompt();

    @Override
    public List<AnalyzedResponse> analyze(List<Review> reviews) {
        var outputConverter = new BeanOutputConverter<>(
                new ParameterizedTypeReference<List<AnalyzedResponse>>() {
                });

        try {
            String sentences = objectMapper
                    .writerWithView(AnalyzeValue.class)
                    .writeValueAsString(reviews);

            return chatClient
                    .prompt()
                    .user(u -> u.text(getPrompt())
                            .param("sentences", sentences) // 데이터 주입
                            .param("format", outputConverter.getFormat())) // 변환 지시어 주입
                    .call()
                    .entity(outputConverter)
                    .stream()
                    .filter(x -> validate(x))
                    .collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public abstract boolean validate(AnalyzedResponse analyzedResponse);
}
