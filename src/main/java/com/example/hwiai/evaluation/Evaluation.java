package com.example.hwiai.evaluation;

import com.example.hwiai.characteristic.Characteristic;
import com.example.hwiai.entity.BaseEntity;
import com.example.hwiai.review.Review;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(
    name = "uk_evaluation_review_characteristic",
    columnNames = {"review_id", "characteristic_id"}
))
public class Evaluation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    private boolean isRelated;

    @ManyToOne
    @JoinColumn(name = "characteristic_id", nullable = false)
    private Characteristic characteristic;

    private int scoreValue;

    private String stringValue;

    @Column(columnDefinition = "TEXT")
    private String phrase;

    public Evaluation(Review review, int scoreValue) {
        this.review = review;
        this.scoreValue = scoreValue;
    }

    public Evaluation(Review review, String stringValue) {
        this.review = review;
        this.stringValue = stringValue;
    }

    // 사용자가 입력한 리뷰 텍스트로부터 AI가 분석, 도출한 점수
    public Evaluation(Review review, boolean isRelated, Characteristic characteristic, int scoreValue, String phrase) {
        this.review = review;
        this.isRelated = isRelated;
        this.characteristic = characteristic;
        this.scoreValue = scoreValue;
        this.phrase = phrase;
    }


    public Evaluation(Review review, boolean isRelated, Characteristic characteristic, String stringValue, String phrase) {
        this.review = review;
        this.isRelated = isRelated;
        this.characteristic = characteristic;
        this.stringValue = null;
        this.phrase = phrase;
    }
}
