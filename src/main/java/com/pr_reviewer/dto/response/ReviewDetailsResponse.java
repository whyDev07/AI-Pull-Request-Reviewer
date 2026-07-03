package com.pr_reviewer.dto.response;

import com.pr_reviewer.entity.ReviewRequestStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ReviewDetailsResponse(
        Long reviewId,
        String summary,
        ReviewRequestStatus status,
        String modelName,
        Long processingTimeMs,
        Integer promptTokens,
        Integer completionTokens,
        Integer totalTokens,
        LocalDateTime createdAt,
        List<ReviewCommentResponse> comments
) {
}
