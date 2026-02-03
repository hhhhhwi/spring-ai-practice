package com.example.hwiai.summary;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hwiai.summary.dto.ReboundSummaryRequest;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class CharacteristicSummaryController {
    private CharacteristicSummaryService characteristicSummaryService;

    @GetMapping("/summary")
    public String getSummary() {
        int sum = 0;
        String returnString = "";
        List<ReboundSummaryRequest> reviews = characteristicSummaryService.analyzeCharacteristics(1L);
        for (ReboundSummaryRequest reboundSummaryDto : reviews) {
            returnString += "! " + sum + "\n";
            if (reboundSummaryDto.isRebound_related()) {
                sum += Sentiment.getScoreByName(reboundSummaryDto.getSentiment());
            }

            returnString += (reboundSummaryDto.isRebound_related() + " : " + reboundSummaryDto.getPhrase() + " "
                    + reboundSummaryDto.getSentiment() + "\n");
        }

        returnString += "\n" + "SUM : " + sum + "\n";
        characteristicSummaryService.saveCharacteristicSummary(1L, sum, reviews.size());
        return returnString;
    }

}
