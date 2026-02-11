package com.example.hwiai.analyzer;

import org.springframework.ai.chat.client.ChatClient;

import com.example.hwiai.analyzer.dto.AnalyzedResponse;
import com.example.hwiai.characteristic.Characteristic;
import com.example.hwiai.characteristic.repository.CharacteristicOptionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ChoiceBasedAnalyzer extends BaseAnalyzer {
    private final CharacteristicOptionRepository characteristicOptionRepository;

    public ChoiceBasedAnalyzer(Characteristic characteristic, ChatClient.Builder chatClientBuilder
        , ObjectMapper objectMapper, CharacteristicOptionRepository characteristicOptionRepository) {
        super(characteristic, chatClientBuilder, objectMapper);
        this.characteristicOptionRepository = characteristicOptionRepository;
    }
    
    @Override
    public abstract String getPrompt();

    @Override
    public boolean validate(AnalyzedResponse response) {
        return characteristicOptionRepository.findByCharacteristicId(getCharacteristic().getId())
            .stream()
            .anyMatch(x -> x.validate(response.getChoiceValue()));
    }
}
