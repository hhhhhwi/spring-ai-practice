package com.example.hwiai.evaluation;

import com.example.hwiai.characteristic.Characteristic;
import com.example.hwiai.characteristic.ValueType;
import com.example.hwiai.characteristic.repository.CharacteristicRepository;
import com.example.hwiai.evaluation.repository.EvaluationRepository;
import com.example.hwiai.product.Product;
import com.example.hwiai.product.repository.ProductRepository;
import com.example.hwiai.review.Review;
import com.example.hwiai.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Evaluation 엔티티의 (review_id, characteristic_id) 유니크 제약 조건 테스트
 * Requirements 3.13: Evaluation 테이블에 (review_id, characteristic_id) 유니크 제약 조건 적용
 */
@SpringBootTest
@Transactional
class EvaluationUniqueConstraintTest {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CharacteristicRepository characteristicRepository;

    private Review review;
    private Characteristic characteristic;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 준비
        Product product = new Product("테스트 상품");
        product = productRepository.save(product);

        review = new Review(product, "테스트 리뷰 텍스트");
        review = reviewRepository.save(review);

        characteristic = new Characteristic("반발력", "프롬프트", ValueType.SCORE, true);
        characteristic = characteristicRepository.save(characteristic);
    }

    @Test
    void 동일한_리뷰와_특성_조합으로_평가를_두_번_저장하면_예외가_발생한다() {
        // Given: 첫 번째 평가 저장
        Evaluation evaluation1 = new Evaluation(review, true, characteristic, 5, "좋은 반발력");
        evaluationRepository.save(evaluation1);
        evaluationRepository.flush();

        // When & Then: 동일한 (review_id, characteristic_id) 조합으로 두 번째 평가 저장 시도
        Evaluation evaluation2 = new Evaluation(review, true, characteristic, 4, "다른 평가");
        
        assertThrows(DataIntegrityViolationException.class, () -> {
            evaluationRepository.save(evaluation2);
            evaluationRepository.flush();
        });
    }

    @Test
    void 다른_리뷰에_대해서는_같은_특성으로_평가를_저장할_수_있다() {
        // Given: 첫 번째 리뷰에 대한 평가
        Evaluation evaluation1 = new Evaluation(review, true, characteristic, 5, "좋은 반발력");
        evaluationRepository.save(evaluation1);

        // 두 번째 리뷰 생성
        Review review2 = new Review(review.getProduct(), "두 번째 리뷰");
        review2 = reviewRepository.save(review2);

        // When: 두 번째 리뷰에 대한 평가 저장
        Evaluation evaluation2 = new Evaluation(review2, true, characteristic, 4, "다른 평가");
        
        // Then: 예외 없이 저장 성공
        assertDoesNotThrow(() -> {
            evaluationRepository.save(evaluation2);
            evaluationRepository.flush();
        });
    }

    @Test
    void 같은_리뷰에_대해_다른_특성으로_평가를_저장할_수_있다() {
        // Given: 첫 번째 특성에 대한 평가
        Evaluation evaluation1 = new Evaluation(review, true, characteristic, 5, "좋은 반발력");
        evaluationRepository.save(evaluation1);

        // 두 번째 특성 생성
        Characteristic characteristic2 = new Characteristic("무게감", "프롬프트", ValueType.SCORE, true);
        characteristic2 = characteristicRepository.save(characteristic2);

        // When: 같은 리뷰에 대해 다른 특성으로 평가 저장
        Evaluation evaluation2 = new Evaluation(review, true, characteristic2, 3, "무거움");
        
        // Then: 예외 없이 저장 성공
        assertDoesNotThrow(() -> {
            evaluationRepository.save(evaluation2);
            evaluationRepository.flush();
        });
    }
}
