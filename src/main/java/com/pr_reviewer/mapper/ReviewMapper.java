package com.pr_reviewer.mapper;

import com.pr_reviewer.entity.*;
import com.pr_reviewer.integration.ai.dto.AiResponse;
import com.pr_reviewer.models.AiReviewComment;
import com.pr_reviewer.models.ReviewResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewMapper {

    public Review toEntity(
            PullRequest pullRequest,
            ReviewResult result,
            AiResponse aiResponse,
            String modelName,
            long processingTime
    ) {

        return Review.builder()
                .pullRequest(pullRequest)
                .summary(result.summary())
                .modelName(modelName)
                .reviewStatus(ReviewRequestStatus.SUCCESS)
                .processingTimeMs(processingTime)
                .promptTokens(aiResponse.usage().promptTokens())
                .completionTokens(aiResponse.usage().completionTokens())
                .totalTokens(aiResponse.usage().totalTokens())
                .build();
    }

    public List<ReviewComment> toComments(
            Review review,
            List<AiReviewComment> comments
    ) {

        if (comments == null || comments.isEmpty()) {
            return List.of();
        }

        return comments.stream()
                .map(comment ->
                        ReviewComment.builder()
                                .review(review)
                                .fileName(comment.fileName())
                                .lineNumber(comment.lineNumber())
                                .severity(Severity.valueOf(comment.severity().toUpperCase()))
                                .reviewCategory(ReviewCategory.valueOf(comment.category().toUpperCase()))
                                .comment(comment.comment())
                                .suggestion(comment.suggestion())
                                .build())
                .toList();
    }
}