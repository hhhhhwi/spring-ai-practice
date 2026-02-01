package com.example.hwiai.summary;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import com.example.hwiai.summary.dto.ReboundSummaryDto;

@Service
public class CharacteristicSummaryService {
  private final CharacteristicSummaryRepository characteristicSummaryRepository;
  private final ChatClient chatClient;

  public CharacteristicSummaryService(CharacteristicSummaryRepository characteristicSummaryRepository,
      ChatClient.Builder chatClientBuilder) {
    this.characteristicSummaryRepository = characteristicSummaryRepository;
    this.chatClient = chatClientBuilder.build();
  }

  public List<ReboundSummaryDto> analyzeCharacteristics() {
    // 출력 컨버터 설정
    var outputConverter = new BeanOutputConverter<>(new ParameterizedTypeReference<List<ReboundSummaryDto>>() {});

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

      String sentences = """

        Sentences:
        [
          {
            "text": "If you're on the hunt for a good, fun, very bouncy easy run shoe, this ticks the boxes. It might look a bit dirty after a few runs, and it has a bit of extra weight to it, but the sheer amount of cushioning and support makes it feel like one of the springiest rides we've tried this year. It's smooth, should last for a good couple hundred miles and generally makes us feel less susceptible to the aches and pains of running. Nike's done it again, folks."
          },
          {
            "text": "Having worn them ourselves now across 5K and 10K runs, as well as at the track, we can confirm that it doesn't feel at all like a speed shoe. This is very much an easy, long-run shoe that has good support and cushioning, rather than a shoe would want to ramp up the pace in. The weight comes in at 11.5 ounces, which is significantly heavier than some of our other easy run shoes (the Asics Novablast, for example, come in at 9 ounces while the Saucony Triumph 22—a shoe we already found quite heavy—is 10 ounces). In general, though, this isn't super noticeable over shorter distances, and the extra cushioning and support meant we felt less ruined after longer runs, making it a price worth paying."
          },
          {
            "text" : "발에 가까운 1차 줌엑스폼이 살짝쿵 푹신하고 편하게 느껴지고 지면에 가까운 2차 리액트폼이 탄탄하게 지면을 튕겨줘. 슈블2가 푹신단단탱글하다가 탕밀어주는데. 보메로도 못지않게 제법 튕겨줘. 그래도 리액트니만큼 슈블만큼은 아니지만 80프로 정도로 튕겨준다. 조깅 말미에 기대이상의 반발력이 너무 궁금해서 3분대로 1k 달려봤는데. 확실히 슈블만큼은 못튕겨줘서 롤링이 힘들었다. 빨리 달리니 튕겨지고 발이 올라오는 과정에서 미드솔 무게가 느껴지더라고. 이게 슈블과의 차이점이더라구. 그래서 빠른 페이스의 템포런과는 맞지 않겠더라. 그래도 기대이상의 탄성이 있다는 걸 확인해서 만족했다.아웃솔 접지력과 내구성도 페가와 같은 와플창이라 매우 좋을듯하네. 장거리는 아니지만 10k를 뛰면서 쿠션이 죽거나 약해진것도 없어서 미드솔 내구성도 매우 좋아 장거리에 딱일것같아"
          }
        ]
      """;

    return chatClient
        .prompt()
        .user(u -> u.text(prompt)
                    .param("sentences", sentences) // 데이터 주입
                    .param("format", outputConverter.getFormat())) // 변환 지시어 주입
        .call()
        .entity(outputConverter);
  }
}