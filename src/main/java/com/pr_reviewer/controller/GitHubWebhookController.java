package com.pr_reviewer.controller;

import com.pr_reviewer.integration.github.webhook.dto.GitHubWebhookPayload;
import com.pr_reviewer.service.GitHubWebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GitHubWebhookController {

    private final GitHubWebhookService webhookService;

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(
            @RequestHeader("X-GitHub-Event") String event,
            @RequestBody GitHubWebhookPayload payload) {

        log.info("Received GitHub webhook event '{}'", event);

        webhookService.processWebhook(event, payload);
        log.info("Successfully processed GitHub webhook event '{}'", event);

        return ResponseEntity.ok().build();
    }
}