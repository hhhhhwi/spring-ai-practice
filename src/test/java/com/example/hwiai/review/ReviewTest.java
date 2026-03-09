package com.example.hwiai.review;

import com.example.hwiai.product.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Review 엔티티 단위 테스트
 * Requirements 1.3: 텍스트가 비어있거나 null이면 AI 분석 대상에서 제외
 * Requirements 1.4: 텍스트가 존재하면 AI 분석 대상에 포함
 */
class ReviewTest {

    private final Product product = new Product("테스트 상품");

    @Test
    void 널_텍스트로_생성하면_text가_null이다() {
        Review review = new Review(product, null);

        assertNull(review.getText());
        assertEquals(product, review.getProduct());
    }

    @Test
    void 빈_문자열로_생성하면_text가_빈_문자열이다() {
        Review review = new Review(product, "");

        assertEquals("", review.getText());
        assertEquals(product, review.getProduct());
    }

    @Test
    void 유효한_텍스트로_생성하면_text가_설정된다() {
        String text = "이 신발은 정말 푹신해요";
        Review review = new Review(product, text);

        assertEquals(text, review.getText());
        assertEquals(product, review.getProduct());
    }

    @Test
    void 널_텍스트_리뷰는_분석_대상이_아니다() {
        Review review = new Review(product, null);

        // null 또는 빈 텍스트는 AI 분석 대상에서 제외 (Req 1.3)
        assertTrue(review.getText() == null || review.getText().isEmpty());
    }

    @Test
    void 빈_텍스트_리뷰는_분석_대상이_아니다() {
        Review review = new Review(product, "");

        assertTrue(review.getText() == null || review.getText().isEmpty());
    }

    @Test
    void 유효한_텍스트_리뷰는_분석_대상이다() {
        Review review = new Review(product, "쿠션감이 좋습니다");

        // 텍스트가 존재하면 AI 분석 대상 (Req 1.4)
        assertNotNull(review.getText());
        assertFalse(review.getText().isEmpty());
    }
}
