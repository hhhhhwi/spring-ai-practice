package com.example.hwiai.search.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.hwiai.aggregation.dto.AggregationResponse;
import com.example.hwiai.aggregation.service.AggregationService;
import com.example.hwiai.characteristic.ValueType;
import com.example.hwiai.product.Product;
import com.example.hwiai.product.repository.ProductRepository;
import com.example.hwiai.search.dto.SearchRequest;
import com.example.hwiai.search.dto.SearchResponse;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AggregationService aggregationService;

    @InjectMocks
    private SearchService searchService;

    private Product product1;
    private Product product2;
    private List<AggregationResponse> aggregations1;
    private List<AggregationResponse> aggregations2;

    @BeforeEach
    void setUp() throws Exception {
        product1 = new Product("Product 1");
        product2 = new Product("Product 2");
        
        // auto-increment 설정의 product id를 리플렉션으로 설정
        setProductId(product1, 1L);
        setProductId(product2, 2L);

        // Product 1: 특성 1은 점수 4, 특성 2는 선택값 "A"
        aggregations1 = Arrays.asList(
            new AggregationResponse(1L, "Characteristic 1", 4, ""),
            new AggregationResponse(2L, "Characteristic 2", 0, "A")
        );

        // Product 2: 특성 1은 점수 3, 특성 2는 선택값 "B"
        aggregations2 = Arrays.asList(
            new AggregationResponse(1L, "Characteristic 1", 3, ""),
            new AggregationResponse(2L, "Characteristic 2", 0, "B")
        );
    }
    
    private void setProductId(Product product, Long id) throws Exception {
        var field = Product.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(product, id);
    }

    @Test
    void 빈_검색조건으로_검색_시_모든_제품을_반환한다() {
        // Given
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));
        when(aggregationService.aggregateAll(product1.getId())).thenReturn(aggregations1);
        when(aggregationService.aggregateAll(product2.getId())).thenReturn(aggregations2);

        // When
        List<SearchResponse> results = searchService.search(Collections.emptyList());

        // Then
        assertEquals(2, results.size());
        verify(productRepository).findAll();
    }

    @Test
    void 점수_검색조건으로_검색_시_조건에_맞는_제품을_반환한다() {
        // Given
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));
        when(aggregationService.aggregateAll(product1.getId())).thenReturn(aggregations1);
        when(aggregationService.aggregateAll(product2.getId())).thenReturn(aggregations2);

        SearchRequest filter = new SearchRequest(1L, ValueType.SCORE, 4.0, null);

        // When
        List<SearchResponse> results = searchService.search(Arrays.asList(filter));

        // Then
        assertEquals(1, results.size());
        assertEquals(product1.getName(), results.get(0).getProductName());
    }

    @Test
    void 선택_검색조건으로_검색_시_조건에_맞는_제품을_반환한다() {
        // Given
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));
        when(aggregationService.aggregateAll(product1.getId())).thenReturn(aggregations1);
        when(aggregationService.aggregateAll(product2.getId())).thenReturn(aggregations2);

        SearchRequest filter = new SearchRequest(2L, ValueType.CHOICE, null, "A");

        // When
        List<SearchResponse> results = searchService.search(Arrays.asList(filter));

        // Then
        assertEquals(1, results.size());
        assertEquals(product1.getName(), results.get(0).getProductName());
    }

    @Test
    void 복합_검색조건으로_검색_시_조건에_맞는_제품을_반환한다() {
        // Given
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));
        when(aggregationService.aggregateAll(product1.getId())).thenReturn(aggregations1);
        when(aggregationService.aggregateAll(product2.getId())).thenReturn(aggregations2);

        SearchRequest scoreFilter = new SearchRequest(1L, ValueType.SCORE, 4.0, null);
        SearchRequest choiceFilter = new SearchRequest(2L, ValueType.CHOICE, null, "A");

        // When
        List<SearchResponse> results = searchService.search(Arrays.asList(scoreFilter, choiceFilter));

        // Then
        assertEquals(1, results.size());
        assertEquals(product1.getName(), results.get(0).getProductName());
    }

    @Test
    void 검색조건에_맞는_제품이_없으면_빈_리스트를_반환한다() {
        // Given
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));
        when(aggregationService.aggregateAll(product1.getId())).thenReturn(aggregations1);
        when(aggregationService.aggregateAll(product2.getId())).thenReturn(aggregations2);

        SearchRequest filter = new SearchRequest(1L, ValueType.SCORE, 5.0, null);

        // When
        List<SearchResponse> results = searchService.search(Arrays.asList(filter));

        // Then
        assertEquals(0, results.size());
    }
}
