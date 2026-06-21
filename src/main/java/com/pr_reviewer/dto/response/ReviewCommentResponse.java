package com.pr_reviewer.dto.response;

import com.pr_reviewer.entity.ReviewCategory;
import com.pr_reviewer.entity.Severity;


public record ReviewCommentResponse(

        String fileName,
        Long lineNumber,
        Severity severity,
        ReviewCategory reviewCategory,
        String comment,
        String suggestion

) {}