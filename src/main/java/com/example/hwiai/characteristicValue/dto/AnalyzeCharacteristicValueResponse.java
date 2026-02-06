package com.example.hwiai.characteristicValue.dto;

public class AnalyzeCharacteristicValueResponse {
    private Long reviewId;

    private boolean isRelated;

    private int value;

    private String phrase;

    public AnalyzeCharacteristicValueResponse(Long reviewId, boolean isRelated, int value, String phrase) {
        this.reviewId = reviewId;
        this.isRelated = isRelated;
        this.value = value;
        this.phrase = phrase;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public boolean isRelated() {
        return isRelated;
    }

    public int getValue() {
        return value;
    }

    public String getPhrase() {
        return phrase;
    }
}
