package com.pr_reviewer.integration.github;

import lombok.Getter;

@Getter
public class GitHubException extends RuntimeException {

    private final int statusCode;

    public GitHubException(int statusCode, String message){
        super(message);
        this.statusCode = statusCode;
    }
}
