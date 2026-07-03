package com.pr_reviewer.dto.response;

import com.pr_reviewer.entity.PullRequestStatus;

import java.time.LocalDateTime;

public record PullRequestResponse(
        Long id,
        Long githubPrId,
        Long prNumber,
        String title,
        String repoOwner,
        String repoName,
        String sourceBranch,
        String targetBranch,
        PullRequestStatus status,
        LocalDateTime createdAt
) {
}
