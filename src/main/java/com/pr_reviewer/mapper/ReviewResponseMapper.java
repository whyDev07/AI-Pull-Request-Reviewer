package com.pr_reviewer.mapper;

import com.pr_reviewer.dto.response.*;
import com.pr_reviewer.entity.PullRequest;
import com.pr_reviewer.entity.Review;
import com.pr_reviewer.entity.ReviewComment;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ReviewResponseMapper {

    public ReviewSummaryResponse toSummary(Review review) {
        return new ReviewSummaryResponse(
                review.getId(),
                review.getSummary(),
                review.getReviewStatus(),
                review.getModelName(),
                review.getProcessingTimeMs(),
                review.getCreatedAt()
        );
    }

    public ReviewDetailsResponse toDetails(Review review) {

        return new ReviewDetailsResponse(
                review.getId(),
                review.getSummary(),
                review.getReviewStatus(),
                review.getModelName(),
                review.getProcessingTimeMs(),
                review.getPromptTokens(),
                review.getCompletionTokens(),
                review.getTotalTokens(),
                review.getCreatedAt(),
                review.getComments()
                        .stream()
                        .map(this::toCommentResponse)
                        .collect(Collectors.toList())
        );
    }

    public ReviewCommentResponse toCommentResponse(ReviewComment comment) {

        return new ReviewCommentResponse(
                comment.getFileName(),
                comment.getLineNumber().longValue(),
                comment.getSeverity(),
                comment.getReviewCategory(),
                comment.getComment(),
                comment.getSuggestion()
        );
    }

    public PullRequestResponse toPullRequestResponse(PullRequest pr) {

        return new PullRequestResponse(
                pr.getId(),
                pr.getGithubPrId(),
                pr.getPrNumber(),
                pr.getTitle(),
                pr.getRepoOwner(),
                pr.getRepoName(),
                pr.getSourceBranch(),
                pr.getTargetBranch(),
                pr.getPrStatus(),
                pr.getCreatedAt()
        );
    }
}