package com.example.hwiai.evaluation.dto;

import com.example.hwiai.characteristic.ValueType;
import com.example.hwiai.evaluation.Evaluation;

import lombok.Getter;

@Getter
public class EvaluationResponse {
    private Long characteristicId;
    private String characteristicName;
    private ValueType valueType;
    private boolean isRelated;
    private int scoreValue;
    private String stringValue;
    private String phrase;

    public static EvaluationResponse of(Evaluation evaluation) {
        return new EvaluationResponse(
            evaluation.getCharacteristic().getId(),
            evaluation.getCharacteristic().getName(),
            evaluation.getCharacteristic().getValueType(),
            evaluation.isRelated(),
            evaluation.getScoreValue(),
            evaluation.getStringValue(),
            evaluation.getPhrase()
        );
    }

    private EvaluationResponse(Long characteristicId, String characteristicName,
                               ValueType valueType, boolean isRelated,
                               int scoreValue, String stringValue, String phrase) {
        this.characteristicId = characteristicId;
        this.characteristicName = characteristicName;
        this.valueType = valueType;
        this.isRelated = isRelated;
        this.scoreValue = scoreValue;
        this.stringValue = stringValue;
        this.phrase = phrase;
    }
}
