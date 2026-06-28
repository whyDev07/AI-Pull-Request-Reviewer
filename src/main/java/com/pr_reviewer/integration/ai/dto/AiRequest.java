package com.pr_reviewer.integration.ai.dto;

import java.util.List;

public record AiRequest(
        String model,
        List<AiMessage> messages

) {}

