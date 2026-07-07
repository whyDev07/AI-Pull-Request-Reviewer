package com.pr_reviewer.service.ai;

import com.pr_reviewer.entity.ReviewCategory;
import com.pr_reviewer.entity.Severity;
import com.pr_reviewer.integration.ai.AiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class EnumMapper {

    public Severity severity(String value) {
        if (value == null)
            throw invalid("severity");

        return switch (value.toUpperCase()) {

            case "LOW" -> Severity.LOW;

            case "MEDIUM", "WARNING" -> Severity.MEDIUM;

            case "HIGH", "ERROR" -> Severity.HIGH;

            case "CRITICAL" -> Severity.CRITICAL;

            default -> throw invalid("severity: " + value);
        };
    }

    public ReviewCategory category(String value) {
        if (value == null)
            throw invalid("category");

        return switch (value.toUpperCase()) {

            case "BUG" -> ReviewCategory.BUG;
            case "SECURITY" -> ReviewCategory.SECURITY;
            case "PERFORMANCE" -> ReviewCategory.PERFORMANCE;
            case "STYLE" -> ReviewCategory.STYLE;
            case "MAINTAINABILITY" -> ReviewCategory.MAINTAINABILITY;
            default -> throw invalid("category: " + value);};
    }

    private AiException invalid(String value) {

        return new AiException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "AI returned unsupported enum value: " + value
        );
    }
}