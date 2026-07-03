package com.pr_reviewer.controller;

import com.pr_reviewer.dto.response.PullRequestResponse;
import com.pr_reviewer.dto.response.ReviewDetailsResponse;
import com.pr_reviewer.dto.response.ReviewSummaryResponse;
import com.pr_reviewer.service.ReviewQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewQueryController {

    private final ReviewQueryService reviewQueryService;

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewSummaryResponse>> getAllReviews() {

        return ResponseEntity.ok(
                reviewQueryService.getAllReviews()
        );
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<ReviewDetailsResponse> getReviewById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                reviewQueryService.getReviewById(id)
        );
    }

    @GetMapping("/pull-requests")
    public ResponseEntity<List<PullRequestResponse>> getAllPullRequests() {

        return ResponseEntity.ok(
                reviewQueryService.getAllPullRequests()
        );
    }

    @GetMapping("/pull-requests/github/{githubPrId}")
    public ResponseEntity<PullRequestResponse> getPullRequestByGithubId(
            @PathVariable Long githubPrId) {

        return ResponseEntity.ok(
                reviewQueryService.getPullRequestByGithubId(githubPrId)
        );
    }

    @GetMapping("/pull-requests/{pullRequestId}/reviews")
    public ResponseEntity<List<ReviewSummaryResponse>> getReviewsOfPullRequest(
            @PathVariable Long pullRequestId) {

        return ResponseEntity.ok(
                reviewQueryService.getReviewsOfPullRequest(pullRequestId)
        );
    }
}