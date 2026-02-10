package com.example.hwiai.analyzer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;

import com.example.hwiai.analyzer.dto.AnalyzedResponse;
import com.example.hwiai.characteristic.Characteristic;
import com.example.hwiai.characteristic.repository.CharacteristicOptionRepository;
import com.example.hwiai.review.Review;
import com.example.hwiai.util.AnalyzeValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ChoiceBasedAnalyzer implements CharacteristicAnalyzer {
    private Characteristic characteristic;
    private final ChatClient chatClient;
    private ObjectMapper objectMapper;
    private CharacteristicOptionRepository characteristicOptionRepository;

    public ChoiceBasedAnalyzer(Characteristic characteristic, ChatClient.Builder chatClientBuilder
        , ObjectMapper objectMapper, CharacteristicOptionRepository characteristicOptionRepository) {
        this.characteristic = characteristic;
        this.chatClient = chatClientBuilder.build();
        this.objectMapper = objectMapper;
        this.characteristicOptionRepository = characteristicOptionRepository;
    }

    @Override
    public Characteristic getCharacteristic() {
        return getCharacteristic();
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
                    .user(u -> u.text(characteristic.getPrompt())
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
    public boolean validate(AnalyzedResponse response) {
        return characteristicOptionRepository.findByCharacteristicId(characteristic.getId())
            .stream()
            .anyMatch(x -> x.validate(response.getChoiceValue()));
    }
}
