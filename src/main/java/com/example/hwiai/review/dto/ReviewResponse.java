package com.example.hwiai.review.dto;

import com.example.hwiai.review.Review;

import lombok.Getter;

@Getter
public class ReviewResponse {
    private Long reviewId;
    private String text;

    public static ReviewResponse of(Review review) {
        return new ReviewResponse(review.getId(), review.getText());
    }

    private ReviewResponse(Long reviewId, String text) {
        this.reviewId = reviewId;
        this.text = text;
    }
}
