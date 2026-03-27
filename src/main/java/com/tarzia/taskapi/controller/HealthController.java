package com.tarzia.taskapi.controller;

import com.tarzia.taskapi.dto.HealthResponseDTO;
import com.tarzia.taskapi.service.HealthService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {
    private final HealthService service;

    public HealthController(HealthService service) {
        this.service = service;
    }

    @GetMapping
    public HealthResponseDTO checkHealth() {
        return service.checkHealth();
    }

}
