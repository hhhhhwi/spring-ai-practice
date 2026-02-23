package com.example.hwiai.characteristic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hwiai.characteristic.CharacteristicOption;

public interface CharacteristicOptionRepository extends JpaRepository<CharacteristicOption, Long> {
    List<CharacteristicOption> findByCharacteristicId(Long characteristicId);
}
