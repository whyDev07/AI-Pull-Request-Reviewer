package com.pr_reviewer.models;

import java.util.List;

public record ReviewResult(
        String summary,
        List<AiReviewComment> comments
) {

    public ReviewResult {
        comments = comments == null
                ? List.of()
                : List.copyOf(comments);
    }

}