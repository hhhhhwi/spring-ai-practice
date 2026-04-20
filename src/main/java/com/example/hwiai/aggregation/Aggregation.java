package com.example.hwiai.aggregation;

import com.example.hwiai.characteristic.ValueType;

import lombok.Getter;

@Getter
public class Aggregation {
    private final Long characteristicId;
    private final String characteristicName;
    private final ValueType valueType;
    private final Integer scoreValue;
    private final String stringValue;

    public static Aggregation score(Long characteristicId, String characteristicName, int scoreValue) {
        return new Aggregation(characteristicId, characteristicName, ValueType.SCORE, scoreValue, null);
    }

    public static Aggregation choice(Long characteristicId, String characteristicName, String stringValue) {
        return new Aggregation(characteristicId, characteristicName, ValueType.CHOICE, null, stringValue);
    }

    private Aggregation(Long characteristicId, String characteristicName, ValueType valueType,
                        Integer scoreValue, String stringValue) {
        this.characteristicId = characteristicId;
        this.characteristicName = characteristicName;
        this.valueType = valueType;
        this.scoreValue = scoreValue;
        this.stringValue = stringValue;
    }
}
