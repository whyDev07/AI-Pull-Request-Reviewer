package com.pr_reviewer.controller;

import com.pr_reviewer.dto.request.ReviewRequest;
import com.pr_reviewer.models.ReviewResult;
import com.pr_reviewer.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResult> review(
            @Valid @RequestBody ReviewRequest request) {

        return ResponseEntity.ok(
                reviewService.pullRequestReview(request)
        );
    }
}