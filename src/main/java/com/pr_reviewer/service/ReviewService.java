package com.pr_reviewer.service;

import com.pr_reviewer.dto.request.ReviewRequest;
import com.pr_reviewer.dto.response.ReviewResponse;
import com.pr_reviewer.integration.github.GitHubClient;
import com.pr_reviewer.models.PullRequestDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final GitHubClient gitHubClient;

    public ReviewResponse pullRequestReview(ReviewRequest request) {
        PullRequestDetails details = gitHubClient.getPullRequest(
                request.owner(),
                request.repository(),
                request.pullRequestNumber(),
                request.githubToken()
        );

        return new ReviewResponse(details);
    }
}
