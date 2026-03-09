package com.example.hwiai.evaluation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.example.hwiai.characteristic.Characteristic;
import com.example.hwiai.characteristic.ValueType;
import com.example.hwiai.error.exception.UniqueConstraintViolationException;
import com.example.hwiai.evaluation.service.EvaluationService;
import com.example.hwiai.product.Product;
import com.example.hwiai.review.Review;

@DataJpaTest
@Import(EvaluationService.class)
public class EvaluationServiceTest {

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private TestEntityManager em;

    @Test
    void 중복_저장시_constraintName으로_RuntimeException을_던진다() {
        // given
        Product product = em.persist(new Product("테스트 상품"));
        Review review = em.persist(new Review(product, "테스트 리뷰"));
        Characteristic characteristic = em.persist(
            new Characteristic("테스트 특징", "분석하세요", ValueType.SCORE, true)
        );

        Evaluation first = new Evaluation(review, false, characteristic, 5, "좋아요");
        evaluationService.save(first);

        // when & then
        Evaluation duplicate = new Evaluation(review, false, characteristic, 3, "또 좋아요");

        assertThatThrownBy(() -> {
            evaluationService.save(duplicate);
        })
        .isInstanceOf(UniqueConstraintViolationException.class)
        .hasMessageContaining("review_id, characteristic_id 값에 대해 unique 하지 않은 값이 등록될 수 없습니다.");
    }


}
