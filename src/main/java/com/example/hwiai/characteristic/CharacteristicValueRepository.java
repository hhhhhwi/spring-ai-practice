package com.example.hwiai.characteristic;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacteristicValueRepository extends JpaRepository<CharacteristicValue, Long> {
    List<CharacteristicValue> findByReviewProductId(Long productId);

}
