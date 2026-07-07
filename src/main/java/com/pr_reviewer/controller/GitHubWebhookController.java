package com.pr_reviewer.controller;

import com.pr_reviewer.integration.github.webhook.dto.GitHubWebhookPayload;
import com.pr_reviewer.service.GitHubWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GitHubWebhookController {

    private final GitHubWebhookService webhookService;

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(
            @RequestHeader("X-GitHub-Event") String event,
            @RequestBody GitHubWebhookPayload payload) {

        System.out.println("EVENT = " + event);

        webhookService.processWebhook(event, payload);

        return ResponseEntity.ok().build();
    }

}
