package com.example.hwiai.characteristic;

import com.example.hwiai.entity.BaseEntity;
import com.example.hwiai.review.Review;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class CharacteristicValue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    private boolean isRelated;

    @Column(name = "score_value")
    private int value;

    @Column(columnDefinition = "TEXT")
    private String phrase;

    // 사용자가 직접 입력한 점수
    public CharacteristicValue(Review review, int value) {
        this.review = review;
        this.value = value;
    }

    // 사용자가 입력한 리뷰 텍스트로부터 AI가 분석, 도출한 점수
    public CharacteristicValue(Review review, boolean isRelated, int value, String phrase) {
        this.review = review;
        this.isRelated = isRelated;
        this.value = value;
        this.phrase = phrase;
    }
}
