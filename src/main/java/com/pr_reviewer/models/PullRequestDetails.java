package com.pr_reviewer.models;

public record PullRequestDetails(

        Long githubId,
        Integer number,

        String title,
        String state,

        String sourceBranch,
        String targetBranch,
        String commitSha
) {
}
