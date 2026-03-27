package com.tarzia.taskapi.service;

import com.tarzia.taskapi.dto.HealthResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class HealthService {
    public HealthResponseDTO checkHealth() {
        return HealthResponseDTO.builder()
                .isOnline(true)
                .build();

    }
}
