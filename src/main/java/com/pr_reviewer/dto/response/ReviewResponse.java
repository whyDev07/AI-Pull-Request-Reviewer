package com.pr_reviewer.dto.response;

import com.pr_reviewer.entity.ReviewRequestStatus;
import com.pr_reviewer.models.PullRequestDetails;

import java.util.List;

public record ReviewResponse (
        PullRequestDetails pullRequestDetails
//        Long reviewId,
//        ReviewRequestStatus reviewStatus,
//        Long processingTimeMs,
//        Integer totalComments,
//        List<ReviewCommentResponse> comments
)
{}