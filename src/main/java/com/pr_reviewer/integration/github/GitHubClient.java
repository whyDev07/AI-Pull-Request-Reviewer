package com.pr_reviewer.integration.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pr_reviewer.integration.github.dto.GitHubFileResponse;
import com.pr_reviewer.integration.github.dto.GitHubPullRequestResponse;
import com.pr_reviewer.models.ChangedFile;
import com.pr_reviewer.models.PullRequestDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GitHubClient {

    private final RestClient restClient;
    private final GitHubProperties githubProperties;

    public PullRequestDetails getPullRequest(//PullRequestDetails so our ReviewService would not depend on GitHub response directly
            String owner,
            String repo,
            Integer pullRequestNumber,
            String token
    ) {
        try {

            // RestClient call
            //First storing the response of pull request then we'll map it to PullRequest Details
            GitHubPullRequestResponse pullRequestResponse =  restClient.get()
                    .uri(uriBuilder -> uriBuilder//Not using hardcoded string concatenation for better design
                            .path("/repos/{owner}/{repo}/pulls/{number}")
                            .build(owner, repo, pullRequestNumber))

                    .header("Authorization",
                            "Bearer " + token)//Github personal access token
                    .retrieve()//sending request now
                    .onStatus(HttpStatusCode::isError,
                            (request, response) -> {
                                int status = response.getStatusCode().value();
                                switch (status) {
                                    case 401 -> throw new GitHubException
                                            (HttpStatus.UNAUTHORIZED, "Invalid GitHub Personal Access Token.");
                                    case 403 -> throw new GitHubException
                                            (HttpStatus.FORBIDDEN, "GitHub rate limit exceeded or access forbidden.");
                                    case 404 -> throw new GitHubException
                                            (HttpStatus.NOT_FOUND, "Repository or Pull Request not found.");
                                    default -> throw new GitHubException
                                            (HttpStatus.BAD_GATEWAY, "GitHub API Error : HTTP " + status);
                                }
                            })
                    .body(GitHubPullRequestResponse.class); //Jackson will automatically map it
            if (pullRequestResponse == null) {
                throw new GitHubException(
                        HttpStatus.BAD_GATEWAY,
                        "GitHub returned an empty response."
                );
            }

            return mapToPullRequestDetails(pullRequestResponse);


        }
        //Handling exception for interrupted or no internet connection
        catch (ResourceAccessException ex) {
            throw new GitHubException(HttpStatus.GATEWAY_TIMEOUT, "Unable to connect to GitHub.");
        }
        catch (RestClientException ex) {
            throw new GitHubException(HttpStatus.BAD_GATEWAY, "Unexpected error while communicating with GitHub.");
        }


    }

    // To map the GHubPullRequestResponse to PullRequestDetails
    private PullRequestDetails mapToPullRequestDetails(
            GitHubPullRequestResponse response
    ) {
        return new PullRequestDetails(
                response.id(),
                response.number(),
                response.title(),
                response.state(),
                response.source().ref(),
                response.target().ref(),
                response.source().sha());//Last commit that Ai should Review

    }

    public List<ChangedFile> getChangedFiles(
            String owner,
            String repo,
            Integer pullRequestNumber,
            String token
    ){
        List<GitHubFileResponse> response = restClient.get()
                        .uri(uriBuilder -> uriBuilder
                                        .path("/repos/{owner}/{repo}/pulls/{number}/files")
                                        .build(owner, repo, pullRequestNumber))
                        .header("Authorization", "Bearer " + token)
                        .retrieve()
                        .body(new ParameterizedTypeReference<>() {});
                        // Java removes generic types at runtime (Type Erasure).
                        // ParameterizedTypeReference tells Spring this JSON array
                        // should be deserialized into List<GitHubFileResponse>.
        if (response == null) {
            return List.of();
        }
        return response.stream()
                .map(this::mapToChangedFile)
                .toList();
    }

    private ChangedFile mapToChangedFile(
            GitHubFileResponse response
    ) { return new ChangedFile(
            response.filename(),
            response.status(),
            response.addition(),
            response.deletion(),
            response.patch()
        );

    }
}
