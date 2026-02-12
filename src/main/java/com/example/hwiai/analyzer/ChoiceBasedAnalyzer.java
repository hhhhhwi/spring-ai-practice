package com.example.hwiai.analyzer;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import com.example.hwiai.analyzer.dto.AnalyzedResponse;
import com.example.hwiai.characteristic.Characteristic;
import com.example.hwiai.characteristic.ValueType;
import com.example.hwiai.characteristic.repository.CharacteristicOptionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import com.example.hwiai.review.Review;

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
    public List<AnalyzedResponse> analyze(Characteristic characteristic, List<Review> reviews) {
        List<AnalyzedResponse> results = super.analyze(characteristic, reviews);
        // validate against characteristic options
        return results.stream()
                .filter(r -> characteristicOptionRepository.findByCharacteristicId(characteristic.getId())
                        .stream()
                        .anyMatch(opt -> opt.validate(r.getChoiceValue())))
                .toList();
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
