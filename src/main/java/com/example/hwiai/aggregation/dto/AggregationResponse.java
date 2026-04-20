package com.example.hwiai.aggregation.dto;

import com.example.hwiai.aggregation.Aggregation;

import lombok.Getter;

@Getter
public class AggregationResponse {
    private final Long characteristicId;
    private final String characteristicName;
    private final int scoreValue;
    private final String stringValue;

    public static AggregationResponse of(Aggregation aggregation) {
        return new AggregationResponse(
            aggregation.getCharacteristicId(),
            aggregation.getCharacteristicName(),
            aggregation.getScoreValue() == null ? 0 : aggregation.getScoreValue(),
            aggregation.getStringValue() == null ? "" : aggregation.getStringValue()
        );
    }

    private AggregationResponse(Long characteristicId, String characteristicName, int scoreValue, String stringValue) {
        this.characteristicId = characteristicId;
        this.characteristicName = characteristicName;
        this.scoreValue = scoreValue;
        this.stringValue = stringValue;
    }
}
