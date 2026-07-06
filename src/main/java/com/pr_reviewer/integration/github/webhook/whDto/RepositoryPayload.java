package com.pr_reviewer.integration.github.webhook.whDto;

public record RepositoryPayload(
        String name,
        OwnerPayload owner

) {}
