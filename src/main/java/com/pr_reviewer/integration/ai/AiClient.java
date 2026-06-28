package com.pr_reviewer.integration.ai;

import com.pr_reviewer.integration.ai.dto.AiMessage;
import com.pr_reviewer.integration.ai.dto.AiRequest;
import com.pr_reviewer.integration.ai.dto.AiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AiClient {

    private final RestClient aiRestClient;
    private final AiProperties aiProperties;


    public AiResponse reviewCode(String prompt){
        try {
            AiRequest request = new AiRequest(
                    aiProperties.getModel(),
                    List.of(new AiMessage(
                                    "system",
                                    """
                                    You are a Senior Java Backend Engineer.
                                    Review only the supplied code.
                                    Never invent issues.
                                    Never assume missing context.
                                    Return only valid JSON.
                                    """
                            ),
                            new AiMessage(
                                    "user",
                                    prompt)));

            return aiRestClient.post()
                    .body(request)
                    .retrieve()
                    .body(AiResponse.class);
        }catch (RestClientException ex){
            throw new AiException("Failed to communicate with OpenRouter.");

        }
    }
}
