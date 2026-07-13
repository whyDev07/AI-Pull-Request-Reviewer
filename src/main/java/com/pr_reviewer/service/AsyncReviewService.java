package com.pr_reviewer.service;

import com.pr_reviewer.dto.request.ReviewRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncReviewService {

    private final ReviewService reviewService;

    @Async("reviewTaskExecutor")
    public void processReview(String owner, String repository,
                              Integer prNumber, String githubToken) {

        log.info("Background review started for PR #{}", prNumber);

        ReviewRequest request = new ReviewRequest(owner, repository, prNumber, githubToken);

        reviewService.pullRequestReview(request);
        log.info("Background review completed for PR #{}", prNumber);
    }
}