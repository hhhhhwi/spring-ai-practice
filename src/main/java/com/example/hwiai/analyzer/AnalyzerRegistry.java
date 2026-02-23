package com.example.hwiai.analyzer;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.hwiai.characteristic.ValueType;

@Component
public class AnalyzerRegistry {
    private final List<CharacteristicAnalyzer> analyzer;

    public AnalyzerRegistry(List<CharacteristicAnalyzer> analyzer) {
        this.analyzer = analyzer;
    }

    public CharacteristicAnalyzer getAnalyzer(ValueType valueType) {
        return analyzer.stream()
                .filter(a -> a.supports(valueType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported value type: " + valueType));
    }
}
