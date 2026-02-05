package com.example.hwiai.characteristic.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hwiai.characteristic.CharacteristicValueService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class CharacteristicValueController {
    private CharacteristicValueService characteristicValueService;

    @GetMapping("/analyzeCharacteristicValue")
    public int analyzeCharacteristicValue() {
        characteristicValueService.saveAnalyzeCharacteristicValue(1L);
        return characteristicValueService.getAverageCharacteristicValue(1L);
    }
}
