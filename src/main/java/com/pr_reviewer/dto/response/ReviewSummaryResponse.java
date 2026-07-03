package com.pr_reviewer.dto.response;

import com.pr_reviewer.entity.ReviewRequestStatus;

import java.time.LocalDateTime;

public record ReviewSummaryResponse(
        Long reviewId,
        String summary,
        ReviewRequestStatus status,
        String modelName,
        Long processingTimeMs,
        LocalDateTime createdAt
) {
}
