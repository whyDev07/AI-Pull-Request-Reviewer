package com.pr_reviewer.exception;

import com.pr_reviewer.integration.ai.AiException;
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

        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(ex.getStatus().value())
                .error(ex.getStatus().getReasonPhrase())
                .message(ex.getMessage())
                .build();

        return ResponseEntity
                .status(ex.getStatus())
                .body(error);
    }


    @ExceptionHandler(AiException.class)
    public ResponseEntity<ApiErrorResponse> handleAiException(
            AiException ex) {

        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(ex.getStatus().value())
                .error(ex.getStatus().getReasonPhrase())
                .message(ex.getMessage())
                .build();

        return ResponseEntity
                .status(ex.getStatus())
                .body(error);
    }
}
