package com.example.hwiai.summary;

public enum Sentiment {
    STRONG_POSITIVE(5),
    POSITIVE(4),
    NEUTRAL(3),
    NEGATIVE(2),
    STRONG_NEGATIVE(1);

    private final int score;

    Sentiment(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public static int getScoreByName(String sentimentName) {
        try {
            return Sentiment.valueOf(sentimentName).getScore();
        } catch (IllegalArgumentException e) {
            return 0; // 기본값으로 0점 반환
        }
    }
}