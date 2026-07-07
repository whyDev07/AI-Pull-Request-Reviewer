package com.pr_reviewer.integration.github.webhook.dto;

public record GitHubWebhookPayload(
        String action,
        RepositoryPayload repository,
        PullRequestPayload pull_request
) {}
