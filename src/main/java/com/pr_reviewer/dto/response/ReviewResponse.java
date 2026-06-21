package com.pr_reviewer.dto.response;

import com.pr_reviewer.entity.ReviewRequestStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private Long reviewId;

    private ReviewRequestStatus reviewStatus;

    private Long processingTimeMs;

    private Integer totalComments;

    private List<ReviewCommentResponse> comments;
}