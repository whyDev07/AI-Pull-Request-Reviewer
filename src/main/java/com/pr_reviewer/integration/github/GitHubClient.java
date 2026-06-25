package com.pr_reviewer.integration.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pr_reviewer.integration.github.dto.GitHubPullRequestResponse;
import com.pr_reviewer.models.PullRequestDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

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
    ) { //First storing the response of pull request then we'll map it to PullRequest Details
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
                            case 401 ->
                                    throw new GitHubException(status ,"Invalid GitHub Personal Access Token.");
                            case 403 ->
                                    throw new GitHubException(status,"GitHub rate limit exceeded or access forbidden.");
                            case 404 ->
                                    throw new GitHubException(status,"Repository or Pull Request not found.");
                            default ->
                                    throw new GitHubException(status, "GitHub API Error : HTTP " + status);
                        }
                    })
                .body(GitHubPullRequestResponse.class); //Jackson will automatically map it

        return mapToPullRequestDetails(pullRequestResponse);

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
}
