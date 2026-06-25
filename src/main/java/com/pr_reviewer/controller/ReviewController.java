package com.pr_reviewer.controller;

import com.pr_reviewer.dto.request.ReviewRequest;
import com.pr_reviewer.dto.response.ReviewResponse;
import com.pr_reviewer.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> review(
            @RequestBody ReviewRequest request){
        return ResponseEntity.ok(
                reviewService.pullRequestReview(request));
    }
}
