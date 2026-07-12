package com.pr_reviewer.service;

import com.pr_reviewer.dto.response.PullRequestResponse;
import com.pr_reviewer.dto.response.ReviewDetailsResponse;
import com.pr_reviewer.dto.response.ReviewSummaryResponse;
import com.pr_reviewer.entity.PullRequest;
import com.pr_reviewer.entity.Review;
import com.pr_reviewer.exception.ApiException;
import com.pr_reviewer.mapper.ReviewResponseMapper;
import com.pr_reviewer.repository.PullRequestRepo;
import com.pr_reviewer.repository.ReviewRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewQueryService {

    private final ReviewRepo reviewRepo;
    private final PullRequestRepo pullRequestRepo;
    private final ReviewResponseMapper mapper;

    public List<ReviewSummaryResponse> getAllReviews() {

        List<ReviewSummaryResponse> reviews = reviewRepo.findAll()
                .stream()
                .map(mapper::toSummary)
                .toList();
        log.info("Retrieved {} review(s).", reviews.size());

        return reviews;
    }

    public ReviewDetailsResponse getReviewById(Long id) {

        log.debug("Looking up review id={}", id);
        Review review = reviewRepo.findById(id)
                .orElseThrow(() ->
                        new ApiException(HttpStatus.NOT_FOUND,
                                "Review not found with id : " + id));

        log.info("Retrieved review id={}", id);
        return mapper.toDetails(review);
    }

    public List<PullRequestResponse> getAllPullRequests() {

        List<PullRequestResponse> pullRequests = pullRequestRepo.findAll()
                .stream()
                .map(mapper::toPullRequestResponse)
                .toList();
        log.info("Retrieved {} pull request(s).", pullRequests.size());

        return pullRequests;
    }

    public PullRequestResponse getPullRequestByGithubId(Long githubPrId) {

        log.debug("Looking up GitHub PR id={}", githubPrId);

        PullRequest pullRequest = pullRequestRepo.findByGithubPrId(githubPrId)
                .orElseThrow(() ->
                        new ApiException(HttpStatus.NOT_FOUND,
                                "Pull Request not found."));
        log.info("Retrieved GitHub PR id={}", githubPrId);

        return mapper.toPullRequestResponse(pullRequest);
    }

    public List<ReviewSummaryResponse> getReviewsOfPullRequest(Long pullRequestId) {

        log.debug("Fetching reviews for Pull Request id={}", pullRequestId);

        List<Review> reviews = reviewRepo.findByPullRequestId(pullRequestId);

        if (reviews.isEmpty()) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No reviews found for Pull Request id: " + pullRequestId);
        }

        log.info("Retrieved {} review(s) for Pull Request id={}",
                reviews.size(),
                pullRequestId);

        return reviews.stream()
                .map(mapper::toSummary)
                .toList();
    }
}