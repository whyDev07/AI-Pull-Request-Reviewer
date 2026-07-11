package com.pr_reviewer.integration.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AiRequest(

        String model,
        List<AiMessage> messages,

        @JsonProperty("response_format")
        ResponseFormat responseFormat

) {}