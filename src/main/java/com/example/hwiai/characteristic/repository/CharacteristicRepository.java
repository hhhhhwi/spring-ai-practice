package com.example.hwiai.characteristic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hwiai.characteristic.Characteristic;

public interface CharacteristicRepository extends JpaRepository<Characteristic, Long> {
    List<Characteristic> findByIsActiveTrue();
}
