package com.example.hwiai.review.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hwiai.review.dto.ReviewRequest;
import com.example.hwiai.review.service.ReviewService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class ReviewController {
    private ReviewService reviewService;

    @GetMapping("/saveReview")
    public void save() {
        ReviewRequest request = new ReviewRequest(1L, null, 3);
        reviewService.saveReview(request);
    }
    
}
