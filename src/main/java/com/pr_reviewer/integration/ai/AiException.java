package com.pr_reviewer.integration.ai;

import com.pr_reviewer.exception.ApiException;
import org.springframework.http.HttpStatus;

public class AiException extends ApiException {

    public AiException(HttpStatus status, String message) {
        super(status , message);
    }

}