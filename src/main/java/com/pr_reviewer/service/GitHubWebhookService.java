package com.pr_reviewer.service;

import com.pr_reviewer.integration.github.GitHubProperties;
import com.pr_reviewer.integration.github.webhook.dto.GitHubWebhookPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GitHubWebhookService {

    private final ReviewService reviewService;
    private final GitHubProperties gitHubProperties;

    public void processWebhook(String event, GitHubWebhookPayload payload)
    {
        if ("ping".equals(event)) {
            return;
        }
        if (!"pull_request".equals(event)) {
            return;
        }
        String action = payload.action();
        if (!action.equals("opened") &&
                !action.equals("reopened") &&
                !action.equals("synchronize")) {
            return;
        }

        reviewService.pullRequestReview(
                payload.repository().owner().login(),
                payload.repository().name(),
                payload.pull_request().number(),
                gitHubProperties.getToken()
        );
    }
}