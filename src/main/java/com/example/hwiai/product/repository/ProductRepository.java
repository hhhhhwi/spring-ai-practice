package com.example.hwiai.product.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.hwiai.product.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
}