package com.example.hwiai.analyzer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.hwiai.analyzer.dto.AnalyzedResponse;
import com.example.hwiai.characteristic.Characteristic;
import com.example.hwiai.characteristic.CharacteristicOption;
import com.example.hwiai.characteristic.ValueType;
import com.example.hwiai.characteristic.repository.CharacteristicOptionRepository;
import com.example.hwiai.product.Product;
import com.example.hwiai.review.Review;

@ExtendWith(MockitoExtension.class)
public class ChoiceBasedAnalyzerTest {
    
    @Mock
    CharacteristicOptionRepository characteristicOptionRepository;

    @Mock
    AnalyzerExecutor analyzerExecutor;

    private final Product product = new Product("product");
    private final Review review = new Review(product, "reviewText");
    private final Characteristic characteristic = createCharacteristic(1L);
    private final CharacteristicOption option1 = new CharacteristicOption(characteristic, "optionValue1");
    private final CharacteristicOption option2 = new CharacteristicOption(characteristic, "optionValue2");
    private final List<CharacteristicOption> options = List.of(option1, option2);

    @Test
    void analyze시_CharacteristicOption에_정의된_값만_반환한다() {
        //given
        ChoiceBasedAnalyzer choiceBasedAnalyzer = new ChoiceBasedAnalyzer(analyzerExecutor, characteristicOptionRepository);
        AnalyzedResponse analyzedResponse = new AnalyzedResponse(1L, true, 0, option1.getOptionValue(), "reviewText");
        given(characteristicOptionRepository.findByCharacteristicId(characteristic.getId())).willReturn(List.of(option1, option2));
        given(analyzerExecutor.execute(any(), any(), any(), any())).willReturn(List.of(analyzedResponse));

        //when
        List<AnalyzedResponse> responses = choiceBasedAnalyzer.analyze(characteristic, List.of(review));

        //then
        Set<String> validOptions = options.stream()
            .map(CharacteristicOption::getOptionValue)
            .collect(Collectors.toSet());
        assertThat(responses).allMatch(response -> validOptions.contains(response.getChoiceValue()));
    }

    @Test
    void validate시_isRelated가_true면_score는_0이고_choice와_phrase는_비어있지_않아야_한다() {
        //given
        ChoiceBasedAnalyzer choiceBasedAnalyzer = new ChoiceBasedAnalyzer(analyzerExecutor, characteristicOptionRepository);

        //then
        assertThat(choiceBasedAnalyzer.validate(relatedResponse(0, option1.getOptionValue(), "reviewText"))).isTrue();
        assertThat(choiceBasedAnalyzer.validate(relatedResponse(1, option1.getOptionValue(), "reviewText"))).isFalse();
        assertThat(choiceBasedAnalyzer.validate(relatedResponse(0, null, "reviewText"))).isFalse();
        assertThat(choiceBasedAnalyzer.validate(relatedResponse(0, " ", "reviewText"))).isFalse();
        assertThat(choiceBasedAnalyzer.validate(relatedResponse(0, option1.getOptionValue(), null))).isFalse();
        assertThat(choiceBasedAnalyzer.validate(relatedResponse(0, option1.getOptionValue(), " "))).isFalse();
    }

    @Test
    void validate시_isRelated가_false면_score는_0이고_choice와_phrase는_null이어야_한다() {
        //given
        ChoiceBasedAnalyzer choiceBasedAnalyzer = new ChoiceBasedAnalyzer(analyzerExecutor, characteristicOptionRepository);

        //then
        assertThat(choiceBasedAnalyzer.validate(unrelatedResponse(0, null, null))).isTrue();
        assertThat(choiceBasedAnalyzer.validate(unrelatedResponse(1, null, null))).isFalse();
        assertThat(choiceBasedAnalyzer.validate(unrelatedResponse(0, option1.getOptionValue(), null))).isFalse();
        assertThat(choiceBasedAnalyzer.validate(unrelatedResponse(0, null, "reviewText"))).isFalse();
    }

    @Test
    void analyze시_validation에_실패한_응답은_제외한다() {
        //given
        ChoiceBasedAnalyzer choiceBasedAnalyzer = new ChoiceBasedAnalyzer(analyzerExecutor, characteristicOptionRepository);
        AnalyzedResponse validRelatedResponse = relatedResponse(0, option1.getOptionValue(), "reviewText");
        AnalyzedResponse invalidChoiceResponse = relatedResponse(0, "invalidOption", "reviewText");
        AnalyzedResponse invalidPhraseResponse = relatedResponse(0, option2.getOptionValue(), " ");
        AnalyzedResponse validUnrelatedResponse = unrelatedResponse(0, null, null);
        AnalyzedResponse invalidUnrelatedResponse = unrelatedResponse(0, option1.getOptionValue(), null);

        given(characteristicOptionRepository.findByCharacteristicId(characteristic.getId())).willReturn(options);
        given(analyzerExecutor.execute(any(), any(), any(), any())).willAnswer(invocation -> {
            Predicate<AnalyzedResponse> validator = invocation.getArgument(3);
            return List.of(
                    validRelatedResponse,
                    invalidChoiceResponse,
                    invalidPhraseResponse,
                    validUnrelatedResponse,
                    invalidUnrelatedResponse)
                    .stream()
                    .filter(validator)
                    .toList();
        });

        //when
        List<AnalyzedResponse> responses = choiceBasedAnalyzer.analyze(characteristic, List.of(review));

        //then
        assertThat(responses).containsExactly(validRelatedResponse, validUnrelatedResponse);
    }

    private Characteristic createCharacteristic(Long id) {
        Characteristic characteristic = new Characteristic("characteristic", "prompt", ValueType.CHOICE, true);
        ReflectionTestUtils.setField(characteristic, "id", id);
        return characteristic;
    }

    private AnalyzedResponse relatedResponse(int scoreValue, String choiceValue, String phrase) {
        return new AnalyzedResponse(1L, true, scoreValue, choiceValue, phrase);
    }

    private AnalyzedResponse unrelatedResponse(int scoreValue, String choiceValue, String phrase) {
        return new AnalyzedResponse(1L, false, scoreValue, choiceValue, phrase);
    }
}
