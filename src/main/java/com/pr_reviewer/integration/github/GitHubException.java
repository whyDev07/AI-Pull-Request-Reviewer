package com.pr_reviewer.integration.github;


import com.pr_reviewer.exception.ApiException;
import org.springframework.http.HttpStatus;

public class GitHubException extends ApiException {

    public GitHubException(HttpStatus statusCode, String message){
        super(statusCode,message);
    }
}
