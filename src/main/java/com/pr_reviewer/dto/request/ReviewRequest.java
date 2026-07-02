package com.pr_reviewer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ReviewRequest(
        @NotBlank
        @Pattern(regexp="[A-Za-z0-9_.-]+")
        String owner,
        String repository,
        Integer pullRequestNumber,
        String githubToken
) {
}
