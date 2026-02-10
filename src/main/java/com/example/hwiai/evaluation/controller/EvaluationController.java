package com.example.hwiai.evaluation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hwiai.evaluation.service.EvaluationService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;


@AllArgsConstructor
@RestController
public class EvaluationController {
    private EvaluationService characteristicValueService;

    @GetMapping("/analyzedCharacteristicValue")
    public int analyzeCharacteristicValue() {
        characteristicValueService.saveAnalyzeCharacteristicValue(1L);
        return characteristicValueService.getAnalyzedCharacteristicValue(1L);
    }

    @GetMapping("/product/{productId}/characteristics")
    public String getMethodName(@RequestParam Long productId) {

        return new String();
    }
    
}
