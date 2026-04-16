package com.example.hwiai.aggregation.dto;

import lombok.Getter;

@Getter
public class AggregationResponse {
    private Long characteristicId;
    private String characteristicName;
    private int scoreValue;
    private String stringValue;

    public static AggregationResponse of(Long characteristicId, String characteristicName, 
                                          int scoreValue, String stringValue) {
        return new AggregationResponse(characteristicId, characteristicName, scoreValue, stringValue);
    }

    private AggregationResponse(Long characteristicId, String characteristicName, int scoreValue, String stringValue) {
        this.characteristicId = characteristicId;
        this.characteristicName = characteristicName;
        this.scoreValue = scoreValue;
        this.stringValue = stringValue;
    }
}
