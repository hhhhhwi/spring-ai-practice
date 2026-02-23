package com.example.hwiai.analyzer;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import com.example.hwiai.analyzer.dto.AnalyzedResponse;
import com.example.hwiai.characteristic.ValueType;
import com.example.hwiai.characteristic.repository.CharacteristicOptionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ChoiceBasedAnalyzer extends BaseAnalyzer {
    private final CharacteristicOptionRepository characteristicOptionRepository;

    public ChoiceBasedAnalyzer(ChatClient.Builder chatClientBuilder,
                               ObjectMapper objectMapper,
                               CharacteristicOptionRepository characteristicOptionRepository) {
        super(chatClientBuilder, objectMapper);
        this.characteristicOptionRepository = characteristicOptionRepository;
    }
    

    @Override
    public boolean validate(AnalyzedResponse response) {
        return true; // validation is done in analyze() with characteristic context
    }

    @Override
    public boolean supports(ValueType valueType) {
        return valueType.equals(ValueType.CHOICE);
    }
}
