package com.tarzia.taskapi.service;

import com.tarzia.taskapi.dto.TaskRequestDTO;
import com.tarzia.taskapi.dto.TaskResponseDTO;
import com.tarzia.taskapi.dto.TaskUpdateDTO;
import com.tarzia.taskapi.entity.Task;
import com.tarzia.taskapi.exception.ResourceNotFoundException;
import com.tarzia.taskapi.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Page<TaskResponseDTO> findAllPaginated(int page, int size) {

        return repository.findAll(PageRequest.of(page, size))
                .map(this::toDTO);
    }

    public TaskResponseDTO findById(Long id) {
        Task task = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return toDTO(task);
    }

    public TaskResponseDTO create(TaskRequestDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());

        Task saved = repository.save(task);
        return toDTO(saved);
    }


    public TaskResponseDTO patch(Long id, TaskUpdateDTO dto) {

        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (dto.getTitle() != null) {
            task.setTitle(dto.getTitle());
        }

        if (dto.getDescription() != null) {
            task.setDescription(dto.getDescription());
        }

        if (dto.getCompleted() != null) {
            task.setCompleted(dto.getCompleted());
        }

        return toDTO(repository.save(task));
    }


    public TaskResponseDTO completeTask(Long id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        task.setCompleted(true);
        return toDTO(repository.save(task));
    }

    public void delete(Long id) {
        Task task = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        repository.delete(task);
    }

    private TaskResponseDTO toDTO(Task task) {
        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .build();
    }
}
