package ru.example.task_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.TaskCreateRequest;
import org.openapitools.model.TaskResponse;
import org.openapitools.model.TaskPatchRequest;
import org.openapitools.api.ApiApi;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import ru.example.task_service.service.TaskService;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController implements ApiApi {

    private final TaskService taskService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello world!";
    }

    @Override
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskCreateRequest taskCreateRequest) {

        log.info("POST /api/v1/tasks - Creating task: {}", taskCreateRequest.getName());
        TaskResponse task = taskService.createTask(taskCreateRequest);

        UriComponentsBuilder uriBuilder = ServletUriComponentsBuilder.fromCurrentRequest();
        URI location = uriBuilder
                .path("/{id}")
                .buildAndExpand(task.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(task);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<TaskResponse> deleteTaskById(@PathVariable UUID id) {
        log.info("POST /api/v1/tasks/{} - Delete task", id);

        TaskResponse task = taskService.deleteTaskById(id);
        return ResponseEntity.ok().body(task);
    }

    @Override
    @GetMapping("/by-date")
    public ResponseEntity<List<TaskResponse>> getTaskByDate(LocalDate date) {
        log.info("POST /api/v1/tasks/by-date - Getting task by date: {}", date);

        List<TaskResponse> tasks = taskService.getTasksByDate(date);
        return ResponseEntity.ok().body(tasks);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable UUID id) {
        log.info("GET /api/v1/tasks - Getting task with id: {}", id);
        TaskResponse task = taskService.getTaskById(id);
        return ResponseEntity.ok().body(task);
    }

    @Override
    public ResponseEntity<List<TaskResponse>> getTasks(Integer page, Integer size) {
        return null;
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> patchTask(UUID id, TaskPatchRequest taskPatchRequest) {
        log.info("PATCH /api/v1/tasks/{} - Update task", id);

        TaskResponse response = taskService.updateTaskChanges(id, taskPatchRequest);
        return ResponseEntity.ok().body(response);
    }

}
