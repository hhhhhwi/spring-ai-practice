package com.example.hwiai.review.dto;

import java.util.List;

import com.example.hwiai.evaluation.dto.EvaluationResponse;
import com.example.hwiai.review.Review;

import lombok.Getter;

@Getter
public class ReviewResponse {
    private Long reviewId;
    private String text;
    private Long productId;
    private String productName;
    private List<EvaluationResponse> evaluations;

    // 리뷰 리스트 조회용 (기본 정보만)
    public static ReviewResponse of(Review review) {
        return new ReviewResponse(
            review.getId(), 
            review.getText(), 
            null, null, null
        );
    }

    // 리뷰 등록 응답용 (reviewId만)
    public static ReviewResponse ofId(Long reviewId) {
        return new ReviewResponse(
            reviewId, 
            null, null, null, null
        );
    }

    // 리뷰 단건 조회용 (모든 정보 + 평가 결과)
    public static ReviewResponse ofDetail(Review review, List<EvaluationResponse> evaluations) {
        return new ReviewResponse(
            review.getId(),
            review.getText(),
            review.getProduct().getId(),
            review.getProduct().getName(),
            evaluations
        );
    }

    private ReviewResponse(Long reviewId, String text,
                          Long productId, String productName,
                          List<EvaluationResponse> evaluations) {
        this.reviewId = reviewId;
        this.text = text;
        this.productId = productId;
        this.productName = productName;
        this.evaluations = evaluations;
    }
}
