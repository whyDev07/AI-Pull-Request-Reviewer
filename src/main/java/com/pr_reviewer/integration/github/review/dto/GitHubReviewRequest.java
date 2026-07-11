package com.pr_reviewer.integration.github.review.dto;

public record GitHubReviewRequest(

        String body,
        String event

) {}