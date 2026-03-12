package com.example.hwiai.analyzer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hwiai.analyzer.service.AnalyzerService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class AnalyzerController {
    private final AnalyzerService analyzerService;

    @GetMapping("/analyzeReflection")
    public String analyzeCharacteristicValue() {
        analyzerService.analyze(1L);
        return "success";
    }

}
