package com.tarzia.taskapi.controller;

import com.tarzia.taskapi.dto.HealthResponseDTO;
import com.tarzia.taskapi.advice.ApiResponseBodyAdvice;
import com.tarzia.taskapi.service.HealthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HealthController.class)
@Import(ApiResponseBodyAdvice.class)
class HealthControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HealthService service;

    @Test
    void shouldReturnHealthStatus() throws Exception {
        when(service.checkHealth()).thenReturn(HealthResponseDTO.builder()
                .isOnline(true)
                .build());

        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.online").value(true))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
