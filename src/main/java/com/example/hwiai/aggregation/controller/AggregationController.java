package com.example.hwiai.aggregation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hwiai.aggregation.dto.AggregationResponse;
import com.example.hwiai.aggregation.service.AggregationService;

@RestController
@RequestMapping("/aggregations")
public class AggregationController {
    private final AggregationService aggregationService;

    public AggregationController(AggregationService aggregationService) {
        this.aggregationService = aggregationService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<List<AggregationResponse>> getAggreations(@PathVariable Long productId) {
        return ResponseEntity.ok().body(aggregationService.aggregateAll(productId));
    }   
}
