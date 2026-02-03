package com.example.hwiai.review.dto;

public class ReviewRequest {
    private Long productId;

    private String text;

    private int scoreValue;

    public ReviewRequest(Long productId, String text, int scoreValue) {
        this.productId = productId;
        this.text = text;
        this.scoreValue = scoreValue;
    }

    public Long getProductId() {
        return productId;
    }

    public String getText() {
        return text;
    }

    public int getScoreValue() {
        return scoreValue;
    }
}