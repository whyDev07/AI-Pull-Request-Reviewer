package com.pr_reviewer.integration.github.review;

import com.pr_reviewer.integration.github.GitHubProperties;
import com.pr_reviewer.integration.github.review.dto.GitHubReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@RequiredArgsConstructor
public class GitHubReviewClient {

    private final RestClient restClient;
    private final GitHubProperties gitHubProperties;

    public void publishReview(String owner, String repo,
                              Integer prNumber, GitHubReviewRequest request) {
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

                                throw new RuntimeException(
                                        "GitHub Review API failed : HTTP "
                                                + res.getStatusCode().value());
                            })
                    .toBodilessEntity();
        } catch (ResourceAccessException ex) {

            throw new RuntimeException(
                    "Unable to connect to GitHub.");
        } catch (RestClientException ex) {
            throw new RuntimeException(
                    "Failed to publish GitHub review.",ex);
        }
    }
}