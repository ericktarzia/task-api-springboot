package com.tarzia.taskapi.service;

import com.tarzia.taskapi.dto.TaskRequestDTO;
import com.tarzia.taskapi.dto.TaskResponseDTO;
import com.tarzia.taskapi.entity.Task;
import com.tarzia.taskapi.exception.ResourceNotFoundException;
import com.tarzia.taskapi.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private  final TaskRepository repository;

    public TaskService(TaskRepository repository){
        this.repository = repository;
    }

    public List<TaskResponseDTO> findAll(){
        return repository.findAll().stream().map(this::toDTO).toList();
    }
    public TaskResponseDTO findById(Long id) {
        Task task = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return toDTO(task);
    }

    public TaskResponseDTO create(TaskRequestDTO dto){
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());

        Task saved = repository.save(task);
        return toDTO(saved);
    }

    public TaskResponseDTO update(Long id, TaskRequestDTO dto){
        Task task = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());

        Task updated = repository.save(task);
        return toDTO(updated);
    }

    public void delete(Long id){
        Task task = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        repository.delete(task);
    }

    public TaskResponseDTO completeTask(Long id){
        Task task = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        task.setCompleted(true);
        Task updated = repository.save(task);
        return toDTO(updated);
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
