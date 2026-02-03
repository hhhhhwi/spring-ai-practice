package com.example.hwiai.summary;

import com.example.hwiai.entity.BaseEntity;
import com.example.hwiai.product.Product;

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
public class CharacteristicSummary extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private double averageScore;

    private int reviewCount;

    public CharacteristicSummary(Product product, double averageScore, int reviewCount) {
        this.product = product;
        this.averageScore = averageScore;
        this.reviewCount = reviewCount;
    }

    public void addScore(double addScore) {
        this.averageScore = ((averageScore * reviewCount) + addScore) / (reviewCount + 1);
        this.reviewCount++;
    }
}
