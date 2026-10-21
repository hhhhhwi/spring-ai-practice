package com.example.hwiai.analyzer;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.example.hwiai.analyzer.dto.AnalyzedResponse;
import com.example.hwiai.review.Review;
import com.example.hwiai.error.exception.UnexpectedAnalyzerResponseException;
import com.example.hwiai.util.AnalyzeValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AnalyzerExecutor {
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public AnalyzerExecutor(ChatClient.Builder chatClientBuilder, ObjectMapper objectMapper) {
        this.chatClient = chatClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public List<AnalyzedResponse> execute(
            Long characteristicId,
            String prompt,
            Map<String, String> promptParams,
            List<Review> reviews,
            Predicate<AnalyzedResponse> validator) {
        var outputConverter = new BeanOutputConverter<>(
                new ParameterizedTypeReference<List<AnalyzedResponse>>() {
                });

        try {
            Set<Long> requestedReviewIds = reviews.stream()
                    .map(Review::getId)
                    .collect(Collectors.toSet());
            String sentences = objectMapper
                    .writerWithView(AnalyzeValue.class)
                    .writeValueAsString(reviews);

            return chatClient
                    .prompt()
                    .user(u -> {
                        u.text(prompt);
                        u.param("sentences", sentences);
                        u.param("format", outputConverter.getFormat());
                        promptParams.forEach(u::param);
                    })
                    .call()
                    .entity(outputConverter)
                    .stream()
                    .filter(response -> {
                        if (response.getReviewId() == null || !requestedReviewIds.contains(response.getReviewId())) {
                            throw new UnexpectedAnalyzerResponseException(response.getReviewId(), characteristicId);
                        }
                        boolean isValid = validator.test(response);
                        if (!isValid) {
                            org.slf4j.LoggerFactory.getLogger(AnalyzerExecutor.class)
                                    .warn("Invalid analyzer response excluded from persistence: {}", response);
                        }
                        return isValid;
                    })
                    .collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
