package com.pr_reviewer.integration.ai;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ai.openrouter")
@Getter
@Setter
public class AiProperties {

    private String baseUrl;
    private String apiKey;
    private String model;

}