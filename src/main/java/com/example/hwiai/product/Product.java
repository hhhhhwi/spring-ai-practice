package com.example.hwiai.product;

import com.example.hwiai.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class Product extends BaseEntity {
    @Id
    @GeneratedValue
    private long id;

    private String name;
}
