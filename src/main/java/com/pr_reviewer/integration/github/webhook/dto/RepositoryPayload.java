package com.pr_reviewer.integration.github.webhook.dto;

public record RepositoryPayload(
        String name,
        OwnerPayload owner

) {}
