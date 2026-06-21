package com.pr_reviewer.dto.response;

import com.pr_reviewer.entity.ReviewCategory;
import com.pr_reviewer.entity.Severity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCommentResponse {

    private String fileName;

    private Long lineNumber;

    private Severity severity;

    private ReviewCategory reviewCategory;

    private String comment;

    private String suggestion;
}