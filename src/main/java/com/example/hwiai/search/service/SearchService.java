package com.example.hwiai.search.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.hwiai.aggregation.dto.AggregationResponse;
import com.example.hwiai.aggregation.service.AggregationService;
import com.example.hwiai.characteristic.Characteristic;
import com.example.hwiai.characteristic.ValueType;
import com.example.hwiai.characteristic.service.CharacteristicService;
import com.example.hwiai.product.Product;
import com.example.hwiai.product.repository.ProductRepository;
import com.example.hwiai.search.dto.SearchRequest;
import com.example.hwiai.search.dto.SearchResponse;

@Service
public class SearchService {
    private final CharacteristicService characteristicService;
    private final AggregationService aggregationService;
    private final ProductRepository productRepository;

    public SearchService(CharacteristicService characteristicService, 
                        AggregationService aggregationService,
                        ProductRepository productRepository) {
        this.characteristicService = characteristicService;
        this.aggregationService = aggregationService;
        this.productRepository = productRepository;
    }

    public List<Characteristic> getFilterableCharacteristics() {
        return characteristicService.findByIsActiveTrue();
    }

    public List<SearchResponse> search(List<SearchRequest> filters) {
        // 빈 필터: 전체 상품 반환
        List<Product> allProducts = productRepository.findAll();
        
        return allProducts.stream()
            .map(product -> {
                List<AggregationResponse> aggregations = aggregationService.aggregateAll(product.getId());
                return new SearchResponse(product.getId(), product.getName(), aggregations);
            })
            .filter(result -> matchesAllFilters(result, filters))
            .collect(Collectors.toList());
    }

    private boolean matchesAllFilters(SearchResponse result, List<SearchRequest> filters) {
        // 빈 필터면 모든 상품 통과
        if (filters == null || filters.isEmpty()) {
            return true;
        }

        // 다중 필터 AND 결합: 모든 필터를 만족해야 함
        return filters.stream().allMatch(filter -> matchesFilter(result, filter));
    }

    private boolean matchesFilter(SearchResponse result, SearchRequest filter) {
        // 해당 특성의 집계 결과 찾기
        AggregationResponse aggregation = result.getCharacteristics().stream()
            .filter(agg -> agg.getCharacteristicId().equals(filter.getCharacteristicId()))
            .findFirst()
            .orElse(null);

        // 집계 결과가 없으면 필터 조건 불만족
        if (aggregation == null) {
            return false;
        }

        // SCORE 필터: N점 이상
        if (filter.getValueType() == ValueType.SCORE) {
            if (filter.getMinScore() == null) {
                return false;
            }
            return aggregation.getScoreValue() >= filter.getMinScore();
        }

        // CHOICE 필터: 최빈값 일치
        if (filter.getValueType() == ValueType.CHOICE) {
            if (filter.getChoiceValue() == null) {
                return false;
            }
            return filter.getChoiceValue().equals(aggregation.getStringValue());
        }

        return false;
    }
}
