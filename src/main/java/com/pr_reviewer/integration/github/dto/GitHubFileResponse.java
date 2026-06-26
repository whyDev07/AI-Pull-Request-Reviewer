package com.pr_reviewer.integration.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubFileResponse(
        @JsonProperty("filename")
        String filename,
        //GitHub returns
        //{"filename":"ReviewService.java"}
        //but our Java field is fileName
        //
        //Without @JsonProperty, Jackson looks for "fileName" and won't populate the field.

        String status,
        Integer addition,
        Integer deletion,
        String patch
) {
}
