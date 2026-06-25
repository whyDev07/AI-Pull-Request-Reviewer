package com.pr_reviewer.dto.request;

public record ReviewRequest(
        String owner,
        String repository,
        Integer pullRequestNumber,
        String githubToken
) {
}
