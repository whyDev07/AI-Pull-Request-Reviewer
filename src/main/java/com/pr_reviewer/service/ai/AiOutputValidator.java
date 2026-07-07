package com.pr_reviewer.service.ai;

import com.pr_reviewer.integration.ai.AiException;
import com.pr_reviewer.models.AiReviewComment;
import com.pr_reviewer.models.ReviewResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AiOutputValidator {

    public void validate(ReviewResult result) {
        if (result == null) {
            throw new AiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "AI returned null response.");
        }

        if (result.summary() == null || result.summary().isBlank()) {
            throw new AiException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "AI returned an invalid review: missing summary.");
        }

        if (result.comments() == null) {return;}

        for (AiReviewComment comment : result.comments()) {

            if (comment.fileName() == null)
                throw invalid("fileName");

            if (comment.comment() == null)
                throw invalid("comment");

            if (comment.suggestion() == null)
                throw invalid("suggestion");

            if (comment.severity() == null)
                throw invalid("severity");

            if (comment.category() == null)
                throw invalid("category");
        }
    }

    private AiException invalid(String field) {
        return new AiException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "AI returned an invalid review: missing " + field + "."
        );
    }
}
