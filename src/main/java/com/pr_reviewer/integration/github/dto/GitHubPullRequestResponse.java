package com.pr_reviewer.integration.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubPullRequestResponse(

        Long id,
        Integer number,
        String title,
        String state,

        @JsonProperty("head")
        GithubBranchRef source,//telling jackson that head should be mapped to source

        @JsonProperty("base")
        GithubBranchRef target//same reason like head
) {
}

