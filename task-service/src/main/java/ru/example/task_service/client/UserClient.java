package ru.example.task_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.openapitools.model.TaskAddRequest;

import java.util.UUID;

@FeignClient("user-service")
public interface UserClient {

    @GetMapping("/api/v1/users/is-exists/{id}")
    public Boolean checkExistsUser(@PathVariable("id") UUID id);

    @PostMapping("/api/v1/users/{id}/tasks")
    public Boolean addTaskForUser(@PathVariable("id") UUID id, @RequestBody TaskAddRequest taskId);

}
