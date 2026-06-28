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

        if (response == null) {
            throw new AiException("AI returned no response.");
        }

        if (response.choices() == null || response.choices().isEmpty()) {
            throw new AiException("AI returned no choices.");
        }

        String json = response.choices()
                .getFirst()
                .message()
                .content();

        json = json.replace("```json", "")
                .replace("```", "")
                .trim();

        try {

            ReviewResult result = objectMapper.readValue(
                    json,
                    ReviewResult.class);
//            if(result == null){
//                result = new ReviewResult(result.summary(), List.of());
//            }
            return result;

        }
        catch (JsonProcessingException e) {

            throw new AiException(
                    "Failed to parse AI response: "
                            + e.getOriginalMessage()
            );
        }
    }
}