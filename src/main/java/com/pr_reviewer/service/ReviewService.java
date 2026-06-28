package com.pr_reviewer.service;

import com.pr_reviewer.dto.request.ReviewRequest;
import com.pr_reviewer.integration.ai.AiClient;
import com.pr_reviewer.integration.ai.dto.AiResponse;
import com.pr_reviewer.integration.github.GitHubClient;
import com.pr_reviewer.models.ChangedFile;
import com.pr_reviewer.models.PullRequestDetails;
import com.pr_reviewer.models.ReviewResult;
import com.pr_reviewer.service.ai.PromptBuilder;
import com.pr_reviewer.service.ai.ReviewParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final GitHubClient gitHubClient;
    private final PromptBuilder promptBuilder;
    private final AiClient aiClient;
    private final ReviewParser reviewParser;

    public ReviewResult pullRequestReview(ReviewRequest request) {

        PullRequestDetails details = gitHubClient.getPullRequest(
                request.owner(),
                request.repository(),
                request.pullRequestNumber(),
                request.githubToken()
        );

        List<ChangedFile> files = gitHubClient.getChangedFiles(
                request.owner(),
                request.repository(),
                request.pullRequestNumber(),
                request.githubToken()
        );

        String prompt = promptBuilder.buildPrompt(details, files);

        AiResponse aiResponse = aiClient.reviewCode(prompt);

        return reviewParser.parse(aiResponse);
    }
}