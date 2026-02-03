package com.example.hwiai.summary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CharacteristicSummaryRepository extends JpaRepository<CharacteristicSummary, Long> {

    @Query("SELECT cs FROM CharacteristicSummary cs WHERE cs.product.id = :productId ORDER BY cs.createdDate DESC LIMIT 1")
    Optional<CharacteristicSummary> findLatestByProductId(@Param("productId") Long productId);
}
