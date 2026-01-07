package ru.example.task_service.controller;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.example.task_service.exception.NotExistsException;
import ru.example.task_service.exception.UserNotFoundException;

@ControllerAdvice
public class ControllerExceptionsHandler {

    @ExceptionHandler(NotExistsException.class)
    public ResponseEntity<?> handleNotExists(NotExistsException e) {
        ErrorResponse error = ErrorResponse.create(e, HttpStatus.NOT_FOUND, "Task not exists");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e) {
        ErrorResponse error = ErrorResponse.create(e, HttpStatus.NOT_FOUND, "User not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException e) {
        if (e.status() == 404) {
            return handleUserNotFound(new UserNotFoundException("User not found"));
        }

        ErrorResponse error = ErrorResponse.create(e, HttpStatus.INTERNAL_SERVER_ERROR, "Service unavailable");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
