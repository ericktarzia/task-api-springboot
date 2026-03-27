package com.tarzia.taskapi.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HealthResponseDTO {
    private boolean isOnline;
}
