package com.pr_reviewer.dto.response;

import com.pr_reviewer.entity.ReviewRequestStatus;

import java.util.List;

public record ReviewResponse (

   Long reviewId,
   ReviewRequestStatus reviewStatus,
   Long processingTimeMs,
   Integer totalComments,
   List<ReviewCommentResponse> comments)
{}