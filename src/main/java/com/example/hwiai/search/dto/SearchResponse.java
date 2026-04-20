package com.example.hwiai.search.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.example.hwiai.aggregation.Aggregation;
import com.example.hwiai.aggregation.dto.AggregationResponse;
import com.example.hwiai.product.Product;

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

    public static SearchResponse of(Product product, List<Aggregation> aggregations) {
        List<AggregationResponse> characteristicResponses = aggregations == null
            ? Collections.emptyList()
            : aggregations.stream()
                .map(AggregationResponse::of)
                .collect(Collectors.toList());

        return new SearchResponse(product.getId(), product.getName(), characteristicResponses);
    }
}
