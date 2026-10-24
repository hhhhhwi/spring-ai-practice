package com.example.hwiai.characteristic.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.hwiai.characteristic.Characteristic;
import com.example.hwiai.characteristic.repository.CharacteristicOptionRepository;
import com.example.hwiai.characteristic.repository.CharacteristicRepository;

@Service
public class CharacteristicService {
    private final CharacteristicRepository characteristicRepository;
    private final CharacteristicOptionRepository characteristicOptionRepository;

    public CharacteristicService(CharacteristicRepository characteristicRepository,
			CharacteristicOptionRepository characteristicOptionRepository) {
		this.characteristicRepository = characteristicRepository;
		this.characteristicOptionRepository = characteristicOptionRepository;
	}

    public List<Characteristic> findByIsActiveTrue() {
        return characteristicRepository.findByIsActiveTrue();
    }
    
}
