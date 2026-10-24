package com.example.hwiai.review.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.hwiai.product.Product;
import com.example.hwiai.product.repository.ProductRepository;
import com.example.hwiai.review.Review;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private ProductRepository productRepository;

    @Test
    void 분석_가능한_리뷰_조회는_null_빈문자열_공백문자열을_제외한다() {
        Product product = productRepository.save(new Product("상품"));
        reviewRepository.save(new Review(product, null));
        reviewRepository.save(new Review(product, ""));
        reviewRepository.save(new Review(product, "   "));
        reviewRepository.save(new Review(product, "\t"));
        reviewRepository.save(new Review(product, "\n"));
        Review analyzable = reviewRepository.save(new Review(product, "분석할 리뷰"));

        assertThat(reviewRepository.findAnalyzableByProductId(product.getId()))
                .extracting(Review::getId)
                .containsExactly(analyzable.getId());
    }
}
