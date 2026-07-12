package com.pr_reviewer.integration.github.review;

import com.pr_reviewer.integration.github.GitHubProperties;
import com.pr_reviewer.integration.github.review.dto.GitHubReviewRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Component
@RequiredArgsConstructor
public class GitHubReviewClient {

    private final RestClient restClient;
    private final GitHubProperties gitHubProperties;

    public void publishReview(String owner, String repo,
                              Integer prNumber, GitHubReviewRequest request) {
        log.info("Publishing AI review to GitHub PR #{}", prNumber);
        try {
            restClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/repos/{owner}/{repo}/pulls/{number}/reviews")
                            .build(owner, repo, prNumber))

                    .header("Authorization",
                            "Bearer " + gitHubProperties.getToken())

                    .body(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError,
                            (req, res) -> {

                                String body = new String(res.getBody().readAllBytes());

                                log.debug("GitHub Review API returned HTTP {}",
                                        res.getStatusCode().value());
                                throw new RuntimeException(
                                        "GitHub Review API failed : HTTP "
                                                + res.getStatusCode().value());
                            })
                    .toBodilessEntity();
            log.info("GitHub review published successfully.");

        } catch (ResourceAccessException ex) {
            log.debug("Unable to connect to GitHub Review API.");
            throw new RuntimeException(
                    "Unable to connect to GitHub.");
        } catch (RestClientException ex) {
            log.debug("Unexpected exception while publishing GitHub review.");
            throw new RuntimeException(
                    "Failed to publish GitHub review.",ex);
        }
    }
}