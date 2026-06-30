package com.pr_reviewer.service;

import com.pr_reviewer.dto.request.ReviewRequest;
import com.pr_reviewer.entity.PullRequest;
import com.pr_reviewer.entity.Review;
import com.pr_reviewer.integration.ai.AiClient;
import com.pr_reviewer.integration.ai.AiProperties;
import com.pr_reviewer.integration.ai.dto.AiResponse;
import com.pr_reviewer.integration.github.GitHubClient;
import com.pr_reviewer.mapper.PullRequestMapper;
import com.pr_reviewer.mapper.ReviewMapper;
import com.pr_reviewer.models.ChangedFile;
import com.pr_reviewer.models.PullRequestDetails;
import com.pr_reviewer.models.ReviewResult;
import com.pr_reviewer.repository.PullRequestRepo;
import com.pr_reviewer.repository.ReviewCommentRepo;
import com.pr_reviewer.repository.ReviewRepo;
import com.pr_reviewer.service.ai.PromptBuilder;
import com.pr_reviewer.service.ai.ReviewParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final GitHubClient gitHubClient;
    private final PromptBuilder promptBuilder;
    private final AiClient aiClient;
    private final ReviewParser reviewParser;

    private final PullRequestRepo pullRequestRepository;
    private final ReviewRepo reviewRepository;
    private final ReviewCommentRepo reviewCommentRepository;

    private final PullRequestMapper pullRequestMapper;
    private final ReviewMapper reviewMapper;

    private final AiProperties aiProperties;

    public ReviewResult pullRequestReview(ReviewRequest request) {

        long start = System.currentTimeMillis();

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
                request.githubToken());

        String prompt = promptBuilder.buildPrompt(details, files);

        AiResponse aiResponse = aiClient.reviewCode(prompt);

        ReviewResult result = reviewParser.parse(aiResponse);

        //Saving all the changes in db
        PullRequest pullRequest =
                pullRequestRepository
                        .findByGithubPrId(details.githubId())
                        .orElseGet(() ->
                                pullRequestRepository.save(
                                        pullRequestMapper.toEntity(details, request)));
        long processingTime = System.currentTimeMillis() - start;
        Review review = reviewMapper.toEntity(
                        pullRequest,
                        result,
                        aiResponse,
                        aiProperties.getModel(),
                        processingTime);
        reviewRepository.save(review);

        reviewCommentRepository.saveAll(reviewMapper.toComments(review, result.comments()));

        return result;

    }
}