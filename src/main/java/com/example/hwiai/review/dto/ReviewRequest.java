package com.example.hwiai.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequest {
    private Long productId;

    private String text;

    public ReviewRequest(Long productId, String text) {
        this.productId = productId;
        this.text = text;
    }
}