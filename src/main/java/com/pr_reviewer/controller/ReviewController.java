package com.pr_reviewer.controller;

import com.pr_reviewer.dto.request.ReviewRequest;
import com.pr_reviewer.models.ReviewResult;
import com.pr_reviewer.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResult> review(
            @Valid @RequestBody ReviewRequest request) {

        log.info("Received manual review request for PR #{} in repository {}/{}",
                request.pullRequestNumber(),
                request.owner(),
                request.repository());

        ReviewResult result = reviewService.pullRequestReview(request);
        log.info("Successfully completed manual review request for PR #{}",
                request.pullRequestNumber());

        return ResponseEntity.ok(result);
    }
}