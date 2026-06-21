package com.pr_reviewer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;


public record PullRequestReviewRequest(

        @NotBlank
        String repositoryOwner,

        @NotBlank
        String repositoryName,

        @Positive
        Integer pullRequestNumber,

        @NotBlank
        String githubToken

) {}