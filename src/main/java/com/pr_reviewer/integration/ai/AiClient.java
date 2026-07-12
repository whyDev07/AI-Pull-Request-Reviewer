package com.pr_reviewer.integration.ai;

import com.pr_reviewer.integration.ai.dto.AiMessage;
import com.pr_reviewer.integration.ai.dto.AiRequest;
import com.pr_reviewer.integration.ai.dto.AiResponse;
import com.pr_reviewer.integration.ai.dto.ResponseFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.net.UnknownHostException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiClient {

    private final RestClient aiRestClient;
    private final AiProperties aiProperties;


    public AiResponse reviewCode(String prompt) {
        log.info("Sending review request to AI model '{}'", aiProperties.getModel());
        try {
            AiRequest request = new AiRequest(

                    aiProperties.getModel(),

                    List.of(

                            new AiMessage(
                                    "system",
                                    """
                                            You are a Senior Java Backend Engineer.
                                            You MUST return ONLY valid JSON.
                                            Never return markdown.
                                            Never wrap JSON inside ```.
                                            Never explain anything.
                                            Never omit required fields.
                                            """),
                            new AiMessage("user", prompt)),
                    new ResponseFormat("json_object"));

            AiResponse response = aiRestClient.post()
                            .body(request)
                            .retrieve()
                            .body(AiResponse.class);

            log.info("AI review completed successfully using model '{}'.", aiProperties.getModel());

            return response;
        }
        catch (HttpClientErrorException.Unauthorized ex) {
            log.debug("OpenRouter rejected API key.");
            throw new AiException(HttpStatus.UNAUTHORIZED, "Invalid OpenRouter API Key.");
        }
        catch (HttpClientErrorException.Forbidden ex) {
            log.debug("OpenRouter denied access.");
            throw new AiException(HttpStatus.FORBIDDEN, "Access denied by OpenRouter.");
        }
        catch (HttpClientErrorException.TooManyRequests ex) {
            log.debug("OpenRouter rate limit exceeded.");
            throw new AiException(HttpStatus.TOO_MANY_REQUESTS, "OpenRouter rate limit exceeded. Please try again later.");
        }
        catch (HttpServerErrorException ex) {
            log.debug("OpenRouter server error.");
            throw new AiException(HttpStatus.BAD_GATEWAY, "OpenRouter is currently unavailable.");
        }
        catch (ResourceAccessException ex) {
            log.debug("Unable to connect to OpenRouter.");
            throw new AiException(HttpStatus.GATEWAY_TIMEOUT, "Unable to connect to OpenRouter.");
        }
        catch (RestClientException ex) {
            log.debug("Unexpected OpenRouter communication error.");
            throw new AiException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error while communicating with OpenRouter.");
        }
    }
}
