package com.pr_reviewer.models;

public record AiReviewComment(
        String fileName,
        Integer lineNumber,
        String severity,
        String category,
        String comment,
        String suggestion

) {}
