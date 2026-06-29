package com.pr_reviewer.service.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pr_reviewer.integration.ai.AiException;
import com.pr_reviewer.integration.ai.dto.AiResponse;
import com.pr_reviewer.models.ReviewResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewParser {

    private final ObjectMapper objectMapper;

    public ReviewResult parse(AiResponse response) {

        if (response == null)
            throw new AiException("AI returned no response.");
        if (response.choices() == null || response.choices().isEmpty())
            throw new AiException("AI returned no choices.");
        if (response.choices().getFirst().message() == null)
            throw new AiException("AI returned no message.");

        String json = response.choices().getFirst().message().content();

        if (json == null || json.isBlank())
            throw new AiException("AI returned empty content.");

        json = response.choices()
                .getFirst()
                .message()
                .content();

        try {
            json = json.replace("```json", "")
                    .replace("```", "")
                    .trim();
            return objectMapper.readValue(json, ReviewResult.class);
        }
        catch (JsonProcessingException e) {
            throw new AiException(
                    "Failed to parse AI response: "
                            + e.getOriginalMessage());
        }catch (Exception e){
            throw new AiException("Failed to parse Ai response : " +
                                      e.getMessage());
        }

    }
}