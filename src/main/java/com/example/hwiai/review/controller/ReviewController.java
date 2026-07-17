package com.example.hwiai.review.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hwiai.evaluation.dto.EvaluationResponse;
import com.example.hwiai.evaluation.repository.EvaluationRepository;
import com.example.hwiai.review.Review;
import com.example.hwiai.review.dto.ReviewRequest;
import com.example.hwiai.review.dto.ReviewResponse;
import com.example.hwiai.review.service.ReviewService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@AllArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private ReviewService reviewService;
    private EvaluationRepository evaluationRepository;

    @GetMapping("/{productId}")
    public ResponseEntity<List<ReviewResponse>> getReviews(@PathVariable Long productId) {
        List<ReviewResponse> reviewResponses = reviewService.findByProductId(productId)
        .stream()
        .map(ReviewResponse::of)
        .toList();

        return ResponseEntity.ok().body(reviewResponses);
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> saveReview(@RequestBody ReviewRequest request) {
        Long reviewId = reviewService.saveReview(request).getId();
        return ResponseEntity
            .created(URI.create("/reviews/detail/" + reviewId))
            .body(ReviewResponse.ofId(reviewId));
    }

    @GetMapping("/detail/{reviewId}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long reviewId) {
        Review review = reviewService.findById(reviewId);
        List<EvaluationResponse> evaluations = evaluationRepository.findByReviewId(reviewId)
            .stream()
            .map(EvaluationResponse::of)
            .toList();

            //evaluation이 필요한지 설계 다시 검토
        
        return ResponseEntity.ok().body(ReviewResponse.ofDetail(review, evaluations));
    }
      
}
