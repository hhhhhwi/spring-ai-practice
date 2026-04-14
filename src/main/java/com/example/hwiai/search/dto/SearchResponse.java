package com.example.hwiai.search.dto;

import java.util.List;

import com.example.hwiai.aggregation.dto.AggregationResponse;

import lombok.Getter;

@Getter
public class SearchResponse {
    private Long productId;
    private String productName;
    private List<AggregationResponse> characteristics;

    public SearchResponse(Long productId, String productName, List<AggregationResponse> characteristics) {
        this.productId = productId;
        this.productName = productName;
        this.characteristics = characteristics;
    }
}
