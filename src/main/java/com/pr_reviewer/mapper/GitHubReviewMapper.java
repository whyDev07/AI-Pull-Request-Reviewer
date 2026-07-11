package com.pr_reviewer.mapper;

import com.pr_reviewer.integration.github.review.dto.GitHubReviewRequest;
import com.pr_reviewer.models.AiReviewComment;
import com.pr_reviewer.models.ReviewResult;
import org.springframework.stereotype.Component;

@Component
public class GitHubReviewMapper {

    public GitHubReviewRequest toRequest(ReviewResult result) {

        StringBuilder body = new StringBuilder();
        body.append("## AI Pull Request Review\n\n");
        body.append("### Summary\n");
        body.append(result.summary()).append("\n\n");

        if (result.comments() == null || result.comments().isEmpty()) {
            body.append("✅ No issues found.");
        } else {
            body.append("### Findings\n\n");
            int i = 1;
            for (AiReviewComment comment : result.comments()) {

                body.append(i++)
                        .append(". **")
                        .append(comment.severity())
                        .append("** - ")
                        .append(comment.category())
                        .append("\n");

                body.append("- File: ")
                        .append(comment.fileName())
                        .append("\n");

                body.append("- Line: ")
                        .append(comment.lineNumber())
                        .append("\n");

                body.append("- Comment: ")
                        .append(comment.comment())
                        .append("\n");

                body.append("- Suggestion: ")
                        .append(comment.suggestion())
                        .append("\n\n");
            }
        }

        return new GitHubReviewRequest(
                body.toString(),
                "COMMENT"
        );
    }
}