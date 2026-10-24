package com.example.hwiai.analyzer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.hwiai.analyzer.dto.AnalyzedResponse;

class ScoreBasedAnalyzerTest {

    private final ScoreBasedAnalyzer analyzer = new ScoreBasedAnalyzer(Mockito.mock(AnalyzerExecutor.class));

    @Test
    void related_응답은_1에서_5점과_비어있지_않은_phrase만_허용한다() {
        assertThat(analyzer.validate(new AnalyzedResponse(1L, true, 1, null, "좋음"))).isTrue();
        assertThat(analyzer.validate(new AnalyzedResponse(1L, true, 5, null, "좋음"))).isTrue();
        assertThat(analyzer.validate(new AnalyzedResponse(1L, true, 0, null, "좋음"))).isFalse();
        assertThat(analyzer.validate(new AnalyzedResponse(1L, true, 6, null, "좋음"))).isFalse();
        assertThat(analyzer.validate(new AnalyzedResponse(1L, true, null, null, "좋음"))).isFalse();
        assertThat(analyzer.validate(new AnalyzedResponse(1L, true, 3, null, " "))).isFalse();
    }

    @Test
    void unrelated_응답은_0점과_null_phrase만_허용한다() {
        assertThat(analyzer.validate(new AnalyzedResponse(1L, false, 0, null, null))).isTrue();
        assertThat(analyzer.validate(new AnalyzedResponse(1L, false, 1, null, null))).isFalse();
        assertThat(analyzer.validate(new AnalyzedResponse(1L, false, null, null, null))).isFalse();
        assertThat(analyzer.validate(new AnalyzedResponse(1L, false, 0, null, "관련 문구"))).isFalse();
    }
}
