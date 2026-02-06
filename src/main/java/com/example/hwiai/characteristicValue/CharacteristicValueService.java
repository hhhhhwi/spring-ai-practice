package com.example.hwiai.characteristicValue;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.example.hwiai.characteristicValue.dto.AnalyzeCharacteristicValueResponse;
import com.example.hwiai.review.Review;
import com.example.hwiai.review.ReviewRepository;
import com.example.hwiai.util.AnalyzeValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CharacteristicValueService {
    private final ChatClient chatClient;
    private final ReviewRepository reviewRepository;
    private final CharacteristicValueRepository characteristicValueRepository;
    private ObjectMapper objectMapper;

    public CharacteristicValueService(ChatClient.Builder chatClientBuilder, ReviewRepository reviewRepository,
            CharacteristicValueRepository characteristicValueRepository, ObjectMapper objectMapper) {
        this.chatClient = chatClientBuilder.build();
        this.reviewRepository = reviewRepository;
        this.characteristicValueRepository = characteristicValueRepository;
        this.objectMapper = objectMapper;
    }

    public int getAverageCharacteristicValue(Long productId) {
        List<CharacteristicValue> characteristicValues = characteristicValueRepository.findByReviewProductId(productId);

        if (characteristicValues.size() == 0) {
            throw new RuntimeException("No characteristic values found for product ID: " + productId);
        }

        return (int) characteristicValues.stream()
                .filter(CharacteristicValue::isRelated)
                .mapToInt(CharacteristicValue::getValue)
                .average()
                .orElse(0.0);
    }

    public List<CharacteristicValue> saveAnalyzeCharacteristicValue(Long productId) {
        List<AnalyzeCharacteristicValueResponse> analyzeCharacteristicValueResponses = analyzeCharacteristicValue(productId);
        
        List<CharacteristicValue> characteristicValues = analyzeCharacteristicValueResponses.stream()
                .map(x -> {
                    Review review = reviewRepository.findById(x.getReviewId())
                            .orElseThrow(() -> new RuntimeException("Review not found with id: " + x.getReviewId()));

                    // Review를 집계됨으로 표시
                    review.markAsAggregated();
                    reviewRepository.save(review);

                    return new CharacteristicValue(review, x.isRelated(), x.getValue(), x.getPhrase());
                })
                .collect(Collectors.toList());
        return characteristicValueRepository.saveAll(characteristicValues);
    }

    public List<AnalyzeCharacteristicValueResponse> analyzeCharacteristicValue(Long productId) {
        List<Review> reviews = reviewRepository.findByProductIdAndIsAggregated(productId, false);

        if (reviews.size() == 0) {
            throw new RuntimeException("No reviews found for product ID: " + productId);
        }

        // 출력 컨버터 설정
        var outputConverter = new BeanOutputConverter<>(
                new ParameterizedTypeReference<List<AnalyzeCharacteristicValueResponse>>() {
                });

        String prompt = """
                You are an expert running shoe review analyst.

                Task:
                Analyze the provided sentences to identify information related to "rebound" or "energy return".

                Constraints:
                1. Do not infer beyond the sentence itself.
                2. If no rebound information is found, set "isRelated" to false and other fields to null.
                3. Return only the JSON code block, no conversational text.
                4. ALWAYS include the reviewId from the input in your output.

                Sentences:
                {sentences}

                Output Field Specifications:
                1. reviewId: The ID from the input (REQUIRED)
                2. isRelated: Set to true if rebound info exists, otherwise false.
                3. value: A numeric score representing the sentiment strength.
                    - Use 5 for STRONG_POSITIVE
                    - Use 4 for POSITIVE
                    - Use 3 for NEUTRAL
                    - Use 2 for NEGATIVE
                    - Use 1 for STRONG_NEGATIVE
                    - Use null if isRelated is false
                4. phrase: Extract the exact matching string from the text. If none, use null.

                CRITICAL VALIDATION RULE:
                    - If isRelated is true, then value MUST be a number (5, 4, 3, 2, or 1) and phrase MUST be a non-empty string.
                    - If isRelated is false, then value MUST be null and phrase MUST be null.
                    - Never return null for value or phrase when isRelated is true.

                Output Format :
                Return ONLY a JSON array. The value should be an array of objects corresponding to each input sentence.
                {format}
                """;

        try {
            String sentences = objectMapper
                    .writerWithView(AnalyzeValue.class)
                    .writeValueAsString(reviews);

            return chatClient
                    .prompt()
                    .user(u -> u.text(prompt)
                            .param("sentences", sentences) // 데이터 주입
                            .param("format", outputConverter.getFormat())) // 변환 지시어 주입
                    .call()
                    .entity(outputConverter);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}