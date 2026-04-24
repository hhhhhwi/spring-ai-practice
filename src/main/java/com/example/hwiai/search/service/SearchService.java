package com.example.hwiai.search.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.hwiai.aggregation.Aggregation;
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

    public List<SearchResponse> search(List<SearchRequest> requests) {
        List<Product> allProducts = productRepository.findAll();
        
        // 빈 필터: 전체 상품 반환 (최적화 불필요)
        if (requests == null || requests.isEmpty()) {
            return allProducts.stream()
                .map(product -> {
                    List<Aggregation> aggregations = aggregationService.aggregateAll(product.getId());
                    return SearchResponse.of(product, aggregations);
                })
                .collect(Collectors.toList());
        }
        
        // 필터가 있는 경우: 필터 조건의 특성만 먼저 집계하여 상품 필터링
        List<Product> filteredProducts = allProducts.stream()
            .filter(product -> matchesAllFiltersOptimized(product.getId(), requests))
            .collect(Collectors.toList());
        
        // 통과한 상품만 전체 특성 집계
        // TODO: 최적화 - 필터링 단계에서 집계한 결과를 재사용하여 중복 집계 방지
        //       현재는 필터 특성을 다시 집계하고 있음 (필터 특성 수가 많을 때 비효율적)
        return filteredProducts.stream()
            .map(product -> {
                List<Aggregation> aggregations = aggregationService.aggregateAll(product.getId());
                return SearchResponse.of(product, aggregations);
            })
            .collect(Collectors.toList());
    }

    private boolean matchesAllFiltersOptimized(Long productId, List<SearchRequest> requests) {
        // 모든 필터 조건을 만족해야 함 (AND 결합)
        return requests.stream().allMatch(request -> matchesFilterOptimized(productId, request));
    }

    private boolean matchesFilterOptimized(Long productId, SearchRequest request) {
        // SCORE 필터: N점 이상
        // TODO SearchService가 aggregateScores / aggregateChoices를 직접 고르지 않게 하는 것이 좋을까?
        if (request.getValueType() == ValueType.SCORE) {
            if (request.getMinScore() == null) {
                return false;
            }
            int scoreValue = aggregationService.aggregateScores(productId, request.getCharacteristicId());
            return scoreValue >= request.getMinScore();
        }

        // CHOICE 필터: 최빈값 일치
        if (request.getValueType() == ValueType.CHOICE) {
            if (request.getChoiceValue() == null) {
                return false;
            }
            String choiceValue = aggregationService.aggregateChoices(productId, request.getCharacteristicId());
            return request.getChoiceValue().equals(choiceValue);
        }

        return false;
    }
}
