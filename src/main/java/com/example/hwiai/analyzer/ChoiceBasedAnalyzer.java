package com.example.hwiai.analyzer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.hwiai.analyzer.dto.AnalyzedResponse;
import com.example.hwiai.characteristic.Characteristic;
import com.example.hwiai.characteristic.CharacteristicOption;
import com.example.hwiai.characteristic.ValueType;
import com.example.hwiai.characteristic.repository.CharacteristicOptionRepository;
import com.example.hwiai.review.Review;

@Component
public class ChoiceBasedAnalyzer implements CharacteristicAnalyzer {
    private static final String CHOICE_PROMPT_SUFFIX = """

            Allowed choice values:
            {options}

            Additional rules:
            1. If isRelated is true, choiceValue must be exactly one of the allowed choice values.
            2. If isRelated is false, choiceValue must be null.
            3. scoreValue must always be 0.
            """;

    private final AnalyzerExecutor analyzerExecutor;
    private final CharacteristicOptionRepository characteristicOptionRepository;

    public ChoiceBasedAnalyzer(AnalyzerExecutor analyzerExecutor,
                               CharacteristicOptionRepository characteristicOptionRepository) {
        this.analyzerExecutor = analyzerExecutor;
        this.characteristicOptionRepository = characteristicOptionRepository;
    }

    @Override
    public List<AnalyzedResponse> analyze(Characteristic characteristic, List<Review> reviews) {
        List<CharacteristicOption> options = characteristicOptionRepository.findByCharacteristicId(characteristic.getId());

        if (options.size() == 0) {
            throw new IllegalStateException(
                    "No characteristic options found for characteristicId=" + characteristic.getId());
        }

        return analyzerExecutor.execute(
                characteristic.getPrompt() + CHOICE_PROMPT_SUFFIX,
                Map.of("options", convertOptionsToPromptValue(options)),
                reviews,
                response -> validate(response) && validateChoice(options, response));
    }

    @Override
    public boolean validate(AnalyzedResponse response) {
        if (response.isRelated()) {
            return response.getScoreValue() == 0
                    && response.getChoiceValue() != null
                    && !response.getChoiceValue().isBlank()
                    && response.getPhrase() != null
                    && !response.getPhrase().isBlank();
        }

        return response.getScoreValue() == 0
                && response.getChoiceValue() == null
                && response.getPhrase() == null;
    }

    @Override
    public boolean supports(ValueType valueType) {
        return valueType.equals(ValueType.CHOICE);
    }

    private String convertOptionsToPromptValue(List<CharacteristicOption> options) {
        return options.stream()
                .map(CharacteristicOption::getOptionValue)
                .collect(Collectors.joining("\", \"", "[\"", "\"]"));
    }

    private boolean validateChoice(List<CharacteristicOption> options, AnalyzedResponse response) {
        if (!response.isRelated()) {
            return true;
        }

        return options.stream()
                .anyMatch(option -> option.validate(response.getChoiceValue()));
    }
}
