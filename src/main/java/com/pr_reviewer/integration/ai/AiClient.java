package com.pr_reviewer.integration.ai;

import com.pr_reviewer.integration.ai.dto.AiMessage;
import com.pr_reviewer.integration.ai.dto.AiRequest;
import com.pr_reviewer.integration.ai.dto.AiResponse;
import lombok.RequiredArgsConstructor;
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
        }catch (HttpClientErrorException.Unauthorized ex) {
            throw new AiException(HttpStatus.UNAUTHORIZED, "Invalid OpenRouter API Key.");
        }
        catch (HttpClientErrorException.Forbidden ex) {
            throw new AiException(HttpStatus.FORBIDDEN, "Access denied by OpenRouter.");
        }
        catch (HttpClientErrorException.TooManyRequests ex) {
            throw new AiException(HttpStatus.TOO_MANY_REQUESTS, "OpenRouter rate limit exceeded. Please try again later.");
        }
        catch (HttpServerErrorException ex) {
            throw new AiException(HttpStatus.BAD_GATEWAY, "OpenRouter is currently unavailable.");
        }
        catch (ResourceAccessException ex) {
            throw new AiException(HttpStatus.GATEWAY_TIMEOUT, "Unable to connect to OpenRouter.");
        }
        catch (RestClientException ex) {
            throw new AiException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error while communicating with OpenRouter.");
        }
    }
}
