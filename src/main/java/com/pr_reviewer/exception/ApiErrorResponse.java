package com.pr_reviewer.exception;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message)
{}