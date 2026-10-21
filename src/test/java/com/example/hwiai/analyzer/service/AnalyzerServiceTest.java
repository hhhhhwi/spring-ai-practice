package com.example.hwiai.analyzer.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.hwiai.analyzer.AnalyzerRegistry;
import com.example.hwiai.analyzer.CharacteristicAnalyzer;
import com.example.hwiai.analyzer.dto.AnalyzedResponse;
import com.example.hwiai.characteristic.Characteristic;
import com.example.hwiai.characteristic.ValueType;
import com.example.hwiai.characteristic.service.CharacteristicService;
import com.example.hwiai.error.exception.AnalysisPreconditionException;
import com.example.hwiai.error.exception.UnexpectedAnalyzerResponseException;
import com.example.hwiai.evaluation.service.EvaluationService;
import com.example.hwiai.product.Product;
import com.example.hwiai.product.repository.ProductRepository;
import com.example.hwiai.review.Review;
import com.example.hwiai.review.service.ReviewService;

@ExtendWith(MockitoExtension.class)
class AnalyzerServiceTest {

    @Mock private ReviewService reviewService;
    @Mock private CharacteristicService characteristicService;
    @Mock private EvaluationService evaluationService;
    @Mock private AnalyzerRegistry analyzerRegistry;
    @Mock private ProductRepository productRepository;
    @Mock private CharacteristicAnalyzer analyzer;
    @Captor private ArgumentCaptor<List<Review>> reviewsCaptor;

    private AnalyzerService analyzerService;
    private final Product product = new Product("상품");
    private final Characteristic characteristic = characteristic(10L);
    private final Review evaluatedReview = review(1L, "이미 평가됨");
    private final Review targetReview = review(2L, "분석 대상");

    @BeforeEach
    void setUp() {
        analyzerService = new AnalyzerService(reviewService, characteristicService, evaluationService, analyzerRegistry, productRepository);
        given(productRepository.existsById(1L)).willReturn(true);
    }

    @Test
    void 특성의_기존_Evaluation이_있는_리뷰는_LLM에_전달하지_않는다() {
        given(reviewService.findAnalyzableByProductId(1L)).willReturn(List.of(evaluatedReview, targetReview));
        given(characteristicService.findByIsActiveTrue()).willReturn(List.of(characteristic));
        given(analyzerRegistry.getAnalyzer(ValueType.SCORE)).willReturn(analyzer);
        given(evaluationService.existsByReviewIdAndCharacteristicId(1L, 10L)).willReturn(true);
        given(evaluationService.existsByReviewIdAndCharacteristicId(2L, 10L)).willReturn(false);
        given(analyzer.analyze(eq(characteristic), any())).willReturn(List.of());

        analyzerService.analyze(1L);

        verify(analyzer).analyze(eq(characteristic), reviewsCaptor.capture());
        org.assertj.core.api.Assertions.assertThat(reviewsCaptor.getValue()).containsExactly(targetReview);
    }

    @Test
    void 요청하지_않은_reviewId를_반환하면_저장하지_않고_실패한다() {
        given(reviewService.findAnalyzableByProductId(1L)).willReturn(List.of(targetReview));
        given(characteristicService.findByIsActiveTrue()).willReturn(List.of(characteristic));
        given(analyzerRegistry.getAnalyzer(ValueType.SCORE)).willReturn(analyzer);
        given(evaluationService.existsByReviewIdAndCharacteristicId(2L, 10L)).willReturn(false);
        given(analyzer.analyze(eq(characteristic), any()))
                .willReturn(List.of(new AnalyzedResponse(999L, true, 5, null, "잘못된 대상")));

        assertThatThrownBy(() -> analyzerService.analyze(1L))
                .isInstanceOf(UnexpectedAnalyzerResponseException.class);

        verify(evaluationService, never()).saveAll(any());
    }

    @Test
    void 분석_가능한_리뷰가_없으면_LLM을_호출하지_않는다() {
        given(reviewService.findAnalyzableByProductId(1L)).willReturn(List.of());

        assertThatThrownBy(() -> analyzerService.analyze(1L))
                .isInstanceOf(AnalysisPreconditionException.class);

        verify(analyzer, never()).analyze(any(), any());
    }

    private Characteristic characteristic(Long id) {
        Characteristic value = new Characteristic("특성", "prompt", ValueType.SCORE, true);
        ReflectionTestUtils.setField(value, "id", id);
        return value;
    }

    private Review review(Long id, String text) {
        Review value = new Review(product, text);
        ReflectionTestUtils.setField(value, "id", id);
        return value;
    }
}
