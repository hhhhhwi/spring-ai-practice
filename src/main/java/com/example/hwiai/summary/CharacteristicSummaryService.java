package com.example.hwiai.summary;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.example.hwiai.product.Product;
import com.example.hwiai.product.ProductRepository;
import com.example.hwiai.review.Review;
import com.example.hwiai.review.ReviewRepository;
import com.example.hwiai.summary.dto.ReboundSummaryDto;

@Service
public class CharacteristicSummaryService {
  private final CharacteristicSummaryRepository characteristicSummaryRepository;
  private final ReviewRepository reviewRepository;
  private final ProductRepository productRepository;
  private final ChatClient chatClient;

  public CharacteristicSummaryService(CharacteristicSummaryRepository characteristicSummaryRepository,
      ReviewRepository reviewRepository, ProductRepository productRepository, ChatClient.Builder chatClientBuilder) {
    this.characteristicSummaryRepository = characteristicSummaryRepository;
    this.reviewRepository = reviewRepository;
    this.productRepository = productRepository;
    this.chatClient = chatClientBuilder.build();
  }

  public List<ReboundSummaryDto> analyzeCharacteristics(Long productId) {
    List<Review> reviews = reviewRepository.findByProductId(productId);

    if (reviews.size() == 0) {
      throw new RuntimeException("No reviews found for product ID: " + productId);
    }

    // 출력 컨버터 설정
    var outputConverter = new BeanOutputConverter<>(new ParameterizedTypeReference<List<ReboundSummaryDto>>() {
    });

    String prompt = """
        You are an expert running shoe review analyst.

        Task:
        Analyze the provided sentences to identify information related to "rebound" or "energy return".

        Constraints:
        1. Do not infer beyond the sentence itself.
        2. If no rebound information is found, set "rebound_related" to false and other fields to null.
        3. Return only the JSON code block, no conversational text.

        Sentences:
        {sentences}

        Output Field Specifications:
        1. rebound_related: Set to true if rebound info exists, otherwise false.
        2. sentiment: Use only these exact values: [STRONG_POSITIVE, POSITIVE, NEUTRAL, NEGATIVE]. If none, use null.
        3. phrase: Extract the exact matching string from the text. If none, use null.

        If 'rebound_related' is true, then 'sentiment' and 'phrase' MUST NOT be null. They must contain valid analyzed values.

        Output Format :
        Return ONLY a JSON array. The value should be an array of objects corresponding to each input sentence.
        {format}
        """;

    // DB에서 가져온 리뷰들을 Stream API로 JSON 형태로 변환
    String sentences = reviews.stream()
        .map(review -> String.format("  {\n    \"text\": \"%s\"\n  }",
            review.getText().replace("\"", "\\\"")))
        .collect(Collectors.joining(",\n", "[\n", "\n]"));

    return chatClient
        .prompt()
        .user(u -> u.text(prompt)
            .param("sentences", sentences) // 데이터 주입
            .param("format", outputConverter.getFormat())) // 변환 지시어 주입
        .call()
        .entity(outputConverter);
  }

  public void saveCharacteristicSummary(Long productId, double averageScore) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

    CharacteristicSummary summary = new CharacteristicSummary(product, averageScore);
    characteristicSummaryRepository.save(summary);
  }
}