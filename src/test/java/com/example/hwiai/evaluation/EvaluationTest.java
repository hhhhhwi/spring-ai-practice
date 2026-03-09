package com.example.hwiai.evaluation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.hwiai.characteristic.Characteristic;
import com.example.hwiai.characteristic.ValueType;
import com.example.hwiai.evaluation.repository.EvaluationRepository;
import com.example.hwiai.product.Product;
import com.example.hwiai.review.Review;

@DataJpaTest
public class EvaluationTest {
    
    @Autowired
    private EvaluationRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    void review와_characteristic가_unique해야한다() {
        //given
        Product product = em.persist(new Product("테스트 상품"));
        Review review = em.persist(new Review(product, "테스트 리뷰"));
        Characteristic characteristic = em.persist(
            new Characteristic("테스트 특징", "분석하세요", ValueType.SCORE, true)
        );
        
        Evaluation evaluation = new Evaluation(review, false, characteristic, null, null);
        repository.save(evaluation);
        
        //when
        //then
        Evaluation dupEvaluation = new Evaluation(review, false, characteristic, null, null);
        
        assertThrows(DataIntegrityViolationException.class, () -> {
            repository.save(dupEvaluation);
        });
    }
}
