package com.example.hwiai.analyzer;

import java.util.List;

import com.example.hwiai.analyzer.dto.AnalyzedResponse;
import com.example.hwiai.characteristic.Characteristic;
import com.example.hwiai.review.Review;

public interface CharacteristicAnalyzer {
    Characteristic getCharacteristic();

    String getPrompt();
    
    List<AnalyzedResponse> analyze(List<Review> reviews);
    
    boolean validate(AnalyzedResponse response);
}
