package com.example.hwiai.analyzer;

import java.util.List;

import com.example.hwiai.analyzer.dto.AnalyzedResponse;
import com.example.hwiai.characteristic.Characteristic;
import com.example.hwiai.characteristic.ValueType;
import com.example.hwiai.review.Review;

public interface CharacteristicAnalyzer {
    List<AnalyzedResponse> analyze(Characteristic characteristic, List<Review> reviews);
    
    boolean validate(AnalyzedResponse response);

    boolean supports(ValueType valueType);
}
