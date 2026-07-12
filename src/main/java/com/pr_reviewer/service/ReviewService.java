package com.pr_reviewer.service;

import com.pr_reviewer.dto.request.ReviewRequest;
import com.pr_reviewer.entity.PullRequest;
import com.pr_reviewer.entity.Review;
import com.pr_reviewer.integration.ai.AiClient;
import com.pr_reviewer.integration.ai.AiProperties;
import com.pr_reviewer.integration.ai.dto.AiResponse;
import com.pr_reviewer.integration.github.GitHubClient;
import com.pr_reviewer.integration.github.review.GitHubReviewClient;
import com.pr_reviewer.integration.github.review.dto.GitHubReviewRequest;
import com.pr_reviewer.mapper.GitHubReviewMapper;
import com.pr_reviewer.mapper.PullRequestMapper;
import com.pr_reviewer.mapper.ReviewMapper;
import com.pr_reviewer.models.ChangedFile;
import com.pr_reviewer.models.PullRequestDetails;
import com.pr_reviewer.models.ReviewResult;
import com.pr_reviewer.repository.PullRequestRepo;
import com.pr_reviewer.repository.ReviewCommentRepo;
import com.pr_reviewer.repository.ReviewRepo;
import com.pr_reviewer.service.ai.AiOutputValidator;
import com.pr_reviewer.service.ai.PromptBuilder;
import com.pr_reviewer.service.ai.ReviewParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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
    private final AiOutputValidator aiOutputValidator;

    private final GitHubReviewClient gitHubReviewClient;
    private final GitHubReviewMapper gitHubReviewMapper;

    public ReviewResult pullRequestReview(ReviewRequest request) {

        long start = System.currentTimeMillis();

        log.info("Starting AI review for PR #{} in repository {}/{}",
                request.pullRequestNumber(),
                request.owner(),
                request.repository());

        PullRequestDetails details = gitHubClient.getPullRequest(
                request.owner(),
                request.repository(),
                request.pullRequestNumber(),
                request.githubToken()
        );
        log.debug("Successfully fetched PR details. GitHub PR Id={}", details.githubId());

        List<ChangedFile> files = gitHubClient.getChangedFiles(
                request.owner(),
                request.repository(),
                request.pullRequestNumber(),
                request.githubToken());
        log.debug("Fetched {} changed file(s) from GitHub.", files.size());

        String prompt = promptBuilder.buildPrompt(details, files);
        log.debug("Prompt generated successfully. Length={} characters.", prompt.length());

        AiResponse aiResponse = aiClient.reviewCode(prompt);

        ReviewResult result = reviewParser.parse(aiResponse);

        aiOutputValidator.validate(result);

        PullRequest pullRequest =
                pullRequestRepository
                        .findByGithubPrId(details.githubId())
                        .orElseGet(() -> {
                            log.info("Pull Request not found in database. Creating new record.");
                            return pullRequestRepository.save(
                                    pullRequestMapper.toEntity(details, request));
                        });

        long processingTime = System.currentTimeMillis() - start;

        Review review = reviewMapper.toEntity(
                pullRequest,
                result,
                aiResponse,
                aiProperties.getModel(),
                processingTime);

        reviewRepository.save(review);
        log.info("Review persisted successfully with id={}", review.getId());

        reviewCommentRepository.saveAll(
                reviewMapper.toComments(review, result.comments()));
        log.info("Saved {} review comment(s).", result.comments().size());

        GitHubReviewRequest reviewRequest =
                gitHubReviewMapper.toRequest(result);

        gitHubReviewClient.publishReview(
                request.owner(),
                request.repository(),
                request.pullRequestNumber(),
                reviewRequest);
        log.info("Successfully published AI review to GitHub.");

        log.info("Review completed successfully in {} ms.",
                processingTime);

        return result;
    }

    public ReviewResult pullRequestReview(
            String owner,
            String repository,
            Integer prNumber,
            String githubToken
    ) {

        ReviewRequest request =
                new ReviewRequest(owner, repository,
                        prNumber, githubToken);

        return pullRequestReview(request);
    }
}