package com.tarzia.taskapi.controller;

import com.tarzia.taskapi.dto.TaskRequestDTO;
import com.tarzia.taskapi.dto.TaskResponseDTO;
import com.tarzia.taskapi.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<TaskResponseDTO> getAllTasks() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public TaskResponseDTO getTaskById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public TaskResponseDTO create(@RequestBody @Valid TaskRequestDTO dto){
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public TaskResponseDTO update(@PathVariable Long id, @RequestBody @Valid TaskRequestDTO dto){
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

    @PatchMapping("/{id}/complete")
    public TaskResponseDTO complete(@PathVariable Long id) {
        return service.completeTask(id);
    }

}
