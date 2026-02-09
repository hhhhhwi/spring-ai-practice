package com.example.hwiai.characteristicValue.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hwiai.characteristicValue.CharacteristicValueService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class CharacteristicValueController {
    private CharacteristicValueService characteristicValueService;

    @GetMapping("/analyzedCharacteristicValue")
    public int analyzeCharacteristicValue() {
        characteristicValueService.saveAnalyzeCharacteristicValue(1L);
        return characteristicValueService.getAnalyzedCharacteristicValue(1L);
    }
}
