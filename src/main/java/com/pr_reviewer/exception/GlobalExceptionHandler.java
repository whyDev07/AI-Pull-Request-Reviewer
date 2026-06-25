package com.pr_reviewer.exception;

import com.pr_reviewer.integration.github.GitHubException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GitHubException.class)
    public ResponseEntity<ApiErrorResponse> handleGitHubException(
            GitHubException ex) {

        ApiErrorResponse error =
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_GATEWAY.value(),
                        "GitHub API Error",
                        ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(error);
    }
}
