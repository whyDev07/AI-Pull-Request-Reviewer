package com.pr_reviewer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PullRequestReviewRequest {

    @NotBlank
    private String repositoryOwner;

    @NotBlank
    private String repositoryName;

    @NotNull
    @Positive
    private Integer pullRequestNumber;

    @NotBlank
    private String githubToken;
}