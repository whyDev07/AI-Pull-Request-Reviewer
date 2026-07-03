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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewQueryService {

    private final ReviewRepo reviewRepo;
    private final PullRequestRepo pullRequestRepo;
    private final ReviewResponseMapper mapper;

    public List<ReviewSummaryResponse> getAllReviews() {

        return reviewRepo.findAll()
                .stream()
                .map(mapper::toSummary)
                .toList();
    }

    public ReviewDetailsResponse getReviewById(Long id) {

        Review review = reviewRepo.findById(id)
                .orElseThrow(() ->
                        new ApiException(HttpStatus.NOT_FOUND,
                                "Review not found with id : " + id));

        return mapper.toDetails(review);
    }

    public List<PullRequestResponse> getAllPullRequests() {

        return pullRequestRepo.findAll()
                .stream()
                .map(mapper::toPullRequestResponse)
                .toList();
    }

    public PullRequestResponse getPullRequestByGithubId(Long githubPrId) {

        PullRequest pullRequest = pullRequestRepo.findByGithubPrId(githubPrId)
                .orElseThrow(() ->
                        new ApiException(HttpStatus.NOT_FOUND,
                                "Pull Request not found."));

        return mapper.toPullRequestResponse(pullRequest);
    }

    public List<ReviewSummaryResponse> getReviewsOfPullRequest(Long pullRequestId) {
        List<Review> reviews = reviewRepo.findByPullRequestId(pullRequestId);

        if (reviews.isEmpty()) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "No reviews found for Pull Request id: " + pullRequestId);
        }
        return reviews.stream()
                .map(mapper::toSummary)
                .toList();

    }
}