package com.example.hwiai.review.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hwiai.review.dto.ReviewResponse;
import com.example.hwiai.review.service.ReviewService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private ReviewService reviewService;

    @GetMapping("/{productId}")
    public ResponseEntity<List<ReviewResponse>> getReviews(@PathVariable Long productId) {
        List<ReviewResponse> reviewResponses = reviewService.findByProductId(productId)
        .stream()
        .map(review -> ReviewResponse.of(review))
        .toList();

        return ResponseEntity.ok().body(reviewResponses);
    }
}
