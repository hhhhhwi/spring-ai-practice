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
import com.example.hwiai.error.exception.AnalysisConfigurationException;
import com.example.hwiai.review.Review;

@Component
public class ChoiceBasedAnalyzer implements CharacteristicAnalyzer {
    private static final String CHOICE_PROMPT_SUFFIX = """

            CRITICAL VALIDATION RULE:
            1. If isRelated is true, choiceValue must be exactly one of the exact strings ( {options} ) and phrase MUST be a non-empty string.
            2. If isRelated is false, then choiceValue MUST be null and phrase MUST be null.
            3. Never return null for choiceValue or phrase when isRelated is true.
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

        if (options.isEmpty()) {
            throw new AnalysisConfigurationException(
                    "No characteristic options found for characteristicId=" + characteristic.getId());
        }

        return analyzerExecutor.execute(
                characteristic.getId(),
                characteristic.getPrompt() + CHOICE_PROMPT_SUFFIX,
                Map.of("options", convertOptionsToPromptValue(options)),
                reviews,
                response -> validate(response) && validateChoice(options, response));
    }

    @Override
    public boolean validate(AnalyzedResponse response) {
        if (response.isRelated()) {
            return response.getChoiceValue() != null
                    && !response.getChoiceValue().isBlank()
                    && response.getPhrase() != null
                    && !response.getPhrase().isBlank();
        }

        return response.getChoiceValue() == null
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
