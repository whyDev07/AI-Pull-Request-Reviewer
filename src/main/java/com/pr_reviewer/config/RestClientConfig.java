package com.pr_reviewer.config;

import com.pr_reviewer.integration.github.GitHubProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final GitHubProperties githubProperties;

    @Bean
    public RestClient restClient() {
        System.out.println("Base URL = " + githubProperties.getBaseUrl());
        return RestClient.builder()
                .baseUrl(githubProperties.getBaseUrl())
                .defaultHeader("Accept", "application/vnd.github+json")
                                //GitHub REST API media type, return as JSON
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                                //This header is recommended by GitHub for the REST API
                                //small improvement but a good production practice
                .build();
    }
}