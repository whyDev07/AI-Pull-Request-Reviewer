package com.pr_reviewer.controller;

import com.pr_reviewer.dto.response.PullRequestResponse;
import com.pr_reviewer.dto.response.ReviewDetailsResponse;
import com.pr_reviewer.dto.response.ReviewSummaryResponse;
import com.pr_reviewer.service.ReviewQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewQueryController {

    private final ReviewQueryService reviewQueryService;

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewSummaryResponse>> getAllReviews() {

        log.info("Fetching all reviews.");
        return ResponseEntity.ok(reviewQueryService.getAllReviews());
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<ReviewDetailsResponse> getReviewById(
            @PathVariable Long id) {

        log.info("Fetching review with id={}", id);
        return ResponseEntity.ok(reviewQueryService.getReviewById(id));
    }

    @GetMapping("/pull-requests")
    public ResponseEntity<List<PullRequestResponse>> getAllPullRequests() {

        log.info("Fetching all pull requests.");
        return ResponseEntity.ok(
                reviewQueryService.getAllPullRequests());
    }

    @GetMapping("/pull-requests/github/{githubPrId}")
    public ResponseEntity<PullRequestResponse> getPullRequestByGithubId(
            @PathVariable Long githubPrId) {

        log.info("Fetching pull request with GitHub id={}", githubPrId);
        return ResponseEntity.ok(reviewQueryService.getPullRequestByGithubId(githubPrId));
    }

    @GetMapping("/pull-requests/{pullRequestId}/reviews")
    public ResponseEntity<List<ReviewSummaryResponse>> getReviewsOfPullRequest(
            @PathVariable Long pullRequestId) {

        log.info("Fetching reviews for pull request id={}", pullRequestId);
        return ResponseEntity.ok(
                reviewQueryService.getReviewsOfPullRequest(pullRequestId));
    }
}