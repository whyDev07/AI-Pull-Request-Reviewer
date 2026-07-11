package com.pr_reviewer.service;

import com.pr_reviewer.integration.github.GitHubProperties;
import com.pr_reviewer.integration.github.webhook.dto.GitHubWebhookPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubWebhookService {

    private final ReviewService reviewService;
    private final GitHubProperties gitHubProperties;

    public void processWebhook(String event, GitHubWebhookPayload payload) {

        if ("ping".equals(event)) {
            log.debug("Ignoring GitHub ping event.");
            return;
        }

        if (!"pull_request".equals(event)) {
            log.debug("Ignoring unsupported GitHub event '{}'.", event);
            return;
        }

        String action = payload.action();

        if (!action.equals("opened") &&
                !action.equals("reopened") &&
                !action.equals("synchronize")) {

            log.debug("Ignoring pull request action '{}'.", action);
            return;
        }

        log.info("Processing '{}' webhook for PR #{} in repository {}/{}",
                action,
                payload.pull_request().number(),
                payload.repository().owner().login(),
                payload.repository().name());

        reviewService.pullRequestReview(
                payload.repository().owner().login(),
                payload.repository().name(),
                payload.pull_request().number(),
                gitHubProperties.getToken()
        );

        log.info("Webhook processing completed for PR #{}",
                payload.pull_request().number());
    }
}