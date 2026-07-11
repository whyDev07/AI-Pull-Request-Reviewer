package com.pr_reviewer.integration.github.review.dto;

public record GitHubReviewComment(

        String path,
        Integer line,
        String body

) {}