package com.example.hwiai.review;

import com.example.hwiai.entity.BaseEntity;
import com.example.hwiai.product.Product;

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
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(columnDefinition = "TEXT")
    private String text;

    private int scoreValue; // 추후 구체화하여 정규화

    public Review(Product product, String text, int scoreValue) {
        this.product = product;
        this.text = text;
        this.scoreValue = scoreValue;
    }
}
