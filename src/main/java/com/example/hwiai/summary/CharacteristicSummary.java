package com.example.hwiai.summary;

import com.example.hwiai.entity.BaseEntity;
import com.example.hwiai.product.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private double averageScore;

    public CharacteristicSummary(Product product, double averageScore) {
        this.product = product;
        this.averageScore = averageScore;
    }
}
