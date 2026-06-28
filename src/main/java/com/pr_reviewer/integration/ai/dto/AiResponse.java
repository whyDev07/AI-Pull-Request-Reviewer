package com.pr_reviewer.integration.ai.dto;

import java.util.List;

public record AiResponse(
        String id,
        List<Choice> choices,
        Usage usage

) {}
