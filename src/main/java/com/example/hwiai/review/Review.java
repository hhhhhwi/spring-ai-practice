package com.example.hwiai.review;

import com.example.hwiai.entity.BaseEntity;
import com.example.hwiai.product.Product;
import com.example.hwiai.util.AnalyzeValue;
import com.fasterxml.jackson.annotation.JsonView;

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
    @JsonView(AnalyzeValue.class)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(columnDefinition = "TEXT")
    @JsonView(AnalyzeValue.class)
    private String text;

    public Review(Product product, String text) {
        this.product = product;
        this.text = text;
    }


    public boolean equalsById(Long reviewId) {
        return this.id.equals(reviewId);
    }
}
