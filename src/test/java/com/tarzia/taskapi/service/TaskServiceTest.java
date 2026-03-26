package com.tarzia.taskapi.service;

import com.tarzia.taskapi.dto.TaskRequestDTO;
import com.tarzia.taskapi.dto.TaskUpdateDTO;
import com.tarzia.taskapi.entity.Task;
import com.tarzia.taskapi.exception.ResourceNotFoundException;
import com.tarzia.taskapi.repository.TaskRepository;
import com.tarzia.taskapi.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    @Test
    void shouldCreateTask() {
        TaskRequestDTO request = new TaskRequestDTO();
        request.setTitle("Nova tarefa");
        request.setDescription("Descricao");

        Task saved = new Task();
        saved.setId(1L);
        saved.setTitle("Nova tarefa");
        saved.setDescription("Descricao");
        saved.setCompleted(false);

        when(repository.save(any(Task.class))).thenReturn(saved);

        var result = service.create(request);

        assertEquals(1L, result.getId());
        assertEquals("Nova tarefa", result.getTitle());
        assertEquals("Descricao", result.getDescription());
        assertFalse(result.isCompleted());
    }

    @Test
    void shouldFindTaskById() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Tarefa");
        task.setDescription("Descricao");

        when(repository.findById(1L)).thenReturn(Optional.of(task));

        var result = service.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Tarefa", result.getTitle());
        assertEquals("Descricao", result.getDescription());
    }

    @Test
    void shouldPatchTaskAsCompleted() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Antes");
        task.setDescription("Antes desc");
        task.setCompleted(false);

        TaskUpdateDTO update = new TaskUpdateDTO();
        update.setTitle("Depois");
        update.setDescription("Depois desc");
        update.setCompleted(true);

        when(repository.findById(1L)).thenReturn(Optional.of(task));
        when(repository.save(task)).thenReturn(task);

        var result = service.patch(1L, update);

        assertEquals("Depois", result.getTitle());
        assertEquals("Depois desc", result.getDescription());
        assertTrue(result.isCompleted());
        verify(repository).save(task);
    }

    @Test
    void shouldDeleteTask() {
        Task task = new Task();
        task.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(task));

        service.delete(1L);

        verify(repository).delete(task);
    }

    @Test
    void shouldThrowWhenTaskNotFoundOnFindById() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void shouldThrowWhenTaskNotFoundOnPatch() {
        TaskUpdateDTO update = new TaskUpdateDTO();
        update.setCompleted(true);

        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.patch(99L, update));
    }

    @Test
    void shouldThrowWhenTaskNotFoundOnDelete() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));
        verify(repository, never()).delete(any(Task.class));
    }

    @Test
    void shouldCompleteTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Tarefa");
        task.setCompleted(false);

        when(repository.findById(1L)).thenReturn(Optional.of(task));
        when(repository.save(task)).thenReturn(task);

        var result = service.completeTask(1L);

        assertTrue(result.isCompleted());
        verify(repository).save(task);
    }

    @Test
    void shouldThrowWhenCompleteTaskNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.completeTask(99L));
        verify(repository, never()).save(any(Task.class));
    }
}