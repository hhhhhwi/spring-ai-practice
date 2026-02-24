package com.example.hwiai.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hwiai.product.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}