
package com.pr_reviewer.mapper;

import com.pr_reviewer.dto.request.ReviewRequest;
import com.pr_reviewer.entity.PullRequest;
import com.pr_reviewer.entity.PullRequestStatus;
import com.pr_reviewer.models.PullRequestDetails;
import org.springframework.stereotype.Component;

@Component
public class PullRequestMapper {

    public PullRequest toEntity(
            PullRequestDetails details,
            ReviewRequest request
    ) {

        return PullRequest.builder()
                .githubPrId(details.githubId())
                .prNumber(details.number().longValue())
                .title(details.title())
                .repoOwner(request.owner())
                .repoName(request.repository())
                .sourceBranch(details.sourceBranch())
                .targetBranch(details.targetBranch())
                .commitSha(details.commitSha())
                .prStatus(PullRequestStatus.valueOf(details.state().toUpperCase()))
                .build();
    }
}