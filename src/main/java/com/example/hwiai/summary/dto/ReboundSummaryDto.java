package com.example.hwiai.summary.dto;

public class ReboundSummaryDto {
    private boolean rebound_related;

    private String sentiment;

    private String phrase;

    public ReboundSummaryDto(boolean rebound_related, String sentiment, String phrase) {
        this.rebound_related = rebound_related;
        this.sentiment = sentiment;
        this.phrase = phrase;
    }

    public boolean isRebound_related() {
        return rebound_related;
    }

    public String getSentiment() {
        return sentiment;
    }

    public String getPhrase() {
        return phrase;
    }
}
