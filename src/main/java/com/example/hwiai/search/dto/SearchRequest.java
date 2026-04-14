package com.example.hwiai.search.dto;

import com.example.hwiai.characteristic.ValueType;

import lombok.Getter;

@Getter
public class SearchRequest {
    private Long characteristicId;
    private ValueType valueType;
    private Double minScore;           // SCORE 타입: N점 이상
    private String choiceValue;        // CHOICE 타입: 선택된 옵션

    public SearchRequest() {
    }

    public SearchRequest(Long characteristicId, ValueType valueType, Double minScore, String choiceValue) {
        this.characteristicId = characteristicId;
        this.valueType = valueType;
        this.minScore = minScore;
        this.choiceValue = choiceValue;
    }
}
