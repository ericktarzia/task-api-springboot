package com.tarzia.taskapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarzia.taskapi.dto.TaskRequestDTO;
import com.tarzia.taskapi.dto.TaskResponseDTO;
import com.tarzia.taskapi.dto.TaskUpdateDTO;
import com.tarzia.taskapi.advice.ApiResponseBodyAdvice;
import com.tarzia.taskapi.exception.GlobalExceptionHandler;
import com.tarzia.taskapi.exception.ResourceNotFoundException;
import com.tarzia.taskapi.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@Import({GlobalExceptionHandler.class, ApiResponseBodyAdvice.class})
class TaskControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService service;

    @Test
    void shouldGetAllTasks() throws Exception {
        TaskResponseDTO task = TaskResponseDTO.builder()
                .id(1L)
                .title("Tarefa")
                .description("Descricao")
                .completed(false)
                .build();

        when(service.findAllPaginated(0, 10)).thenReturn(new PageImpl<>(List.of(task)));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("Tarefa"));
    }

    @Test
    void shouldGetTaskById() throws Exception {
        TaskResponseDTO task = TaskResponseDTO.builder()
                .id(1L)
                .title("Tarefa")
                .description("Descricao")
                .completed(false)
                .build();

        when(service.findById(1L)).thenReturn(task);

        mockMvc.perform(get("/tasks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Tarefa"));
    }

    @Test
    void shouldReturn404WhenTaskNotFoundById() throws Exception {
        when(service.findById(99L)).thenThrow(new ResourceNotFoundException("Task not found"));

        mockMvc.perform(get("/tasks/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Task not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldCreateTask() throws Exception {
        TaskRequestDTO request = new TaskRequestDTO();
        request.setTitle("Nova tarefa");
        request.setDescription("Descricao");

        TaskResponseDTO response = TaskResponseDTO.builder()
                .id(1L)
                .title("Nova tarefa")
                .description("Descricao")
                .completed(false)
                .build();

        when(service.create(any(TaskRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Nova tarefa"));
    }

    @Test
    void shouldReturn400WhenCreateValidationFails() throws Exception {
        TaskRequestDTO request = new TaskRequestDTO();
        request.setTitle(" ");
        request.setDescription("Descricao");

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("title: must not be blank"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldPatchTask() throws Exception {
        TaskUpdateDTO update = new TaskUpdateDTO();
        update.setCompleted(true);

        TaskResponseDTO response = TaskResponseDTO.builder()
                .id(1L)
                .title("Tarefa")
                .description("Descricao")
                .completed(true)
                .build();

        when(service.patch(any(Long.class), any(TaskUpdateDTO.class))).thenReturn(response);

        mockMvc.perform(patch("/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.completed").value(true));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(service).delete(1L);
    }

    @Test
    void shouldReturn404WhenDeleteTaskNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Task not found")).when(service).delete(99L);

        mockMvc.perform(delete("/tasks/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Task not found"));
    }

    @Test
    void shouldCompleteTask() throws Exception {
        TaskResponseDTO response = TaskResponseDTO.builder()
                .id(1L)
                .title("Tarefa")
                .description("Descricao")
                .completed(true)
                .build();

        when(service.completeTask(1L)).thenReturn(response);

        mockMvc.perform(patch("/tasks/{id}/complete", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.completed").value(true));
    }

    @Test
    void shouldReturn404WhenCompleteTaskNotFound() throws Exception {
        when(service.completeTask(99L)).thenThrow(new ResourceNotFoundException("Task not found"));

        mockMvc.perform(patch("/tasks/{id}/complete", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Task not found"));
    }
}

