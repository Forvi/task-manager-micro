package ru.user.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.CreateTaskForUserRequest;
import org.openapitools.model.UserCreateRequest;
import org.openapitools.model.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.openapitools.api.ApiApi;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import ru.user.example.service.UserService;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Validated
public class UserController implements ApiApi {

    private final UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello world!";
    }

    @Override
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserCreateRequest userCreateRequest) {

        log.info("POST /api/v1/users - Creating user: {}", userCreateRequest.getUsername());
        UserResponse user = userService.createUser(userCreateRequest);

        UriComponentsBuilder uriBuilder = ServletUriComponentsBuilder.fromCurrentRequest();
        URI location = uriBuilder
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(user);
    }

    @Override
    @PostMapping("/{id}/tasks")
    public ResponseEntity<UserResponse> createTaskForUser(
            @PathVariable UUID id,
            @RequestBody CreateTaskForUserRequest createTaskForUserRequest) {
        log.info("POST /api/v1/users/{id}/tasks - Adding task for user: {}, task: {}",
                id, createTaskForUserRequest.getTaskId());

        UserResponse response = userService.addTaskForUser(id, createTaskForUserRequest.getTaskId());
        return ResponseEntity.ok().body(response);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        log.info("GET /api/v1/users - Getting user with id: {}", id);
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok().body(user);
    }

    @Override
    public ResponseEntity<List<UserResponse>> getUsers(Integer page, Integer size) {
        return null;
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> softDeleteUserById(UUID id) {
        UserResponse user = userService.softDelete(id);
        return ResponseEntity.ok().body(user);
    }

    @Override
    @GetMapping("/is-exists/{id}")
    public ResponseEntity<Boolean> checkExistsUser(@PathVariable UUID id) {
        log.info("GET /api/v1/users/is-exists - Checking user with id: {}", id);
        boolean exists = userService.isExist(id);
        return ResponseEntity.ok().body(exists);
    }
}
