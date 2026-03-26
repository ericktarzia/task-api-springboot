package com.tarzia.taskapi.controller;

import com.tarzia.taskapi.dto.TaskRequestDTO;
import com.tarzia.taskapi.dto.TaskResponseDTO;
import com.tarzia.taskapi.dto.TaskUpdateDTO;
import com.tarzia.taskapi.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public Page<TaskResponseDTO> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return service.findAllPaginated(page, size);
    }

    @GetMapping("/{id}")
    public TaskResponseDTO getTaskById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public TaskResponseDTO create(@RequestBody @Valid TaskRequestDTO dto) {
        return service.create(dto);
    }

    @PatchMapping("/{id}")
    public TaskResponseDTO patch(@PathVariable Long id, @RequestBody TaskUpdateDTO dto) {
        return service.patch(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


}
