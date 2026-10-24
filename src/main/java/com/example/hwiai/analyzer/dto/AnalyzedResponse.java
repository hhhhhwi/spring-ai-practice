package com.example.hwiai.analyzer.dto;

public class AnalyzedResponse {
    private Long reviewId;

    private boolean isRelated;

    private Integer scoreValue;

    private String choiceValue;

    private String phrase;

    public AnalyzedResponse(Long reviewId, boolean isRelated, Integer scoreValue, String choiceValue, String phrase) {
        this.reviewId = reviewId;
        this.isRelated = isRelated;
        this.scoreValue = scoreValue;
        this.choiceValue = choiceValue;
        this.phrase = phrase;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public boolean isRelated() {
        return isRelated;
    }

    public Integer getScoreValue() {
        return scoreValue;
    }

    public String getChoiceValue() {
        return choiceValue;
    }

    public String getPhrase() {
        return phrase;
    }

    @Override
    public String toString() {
        return "AnalyzedResponse[reviewId=" + reviewId
                + ", isRelated=" + isRelated
                + ", scoreValue=" + scoreValue
                + ", choiceValue=" + choiceValue
                + ", phrase=" + phrase + "]";
    }
}
