package ru.user.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.user.example.exception.AlreadyExistsException;
import ru.user.example.exception.InvalidPasswordLengthException;
import ru.user.example.exception.NotExistsException;

@ControllerAdvice
public class ControllerExceptionsHandler {

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<?> handleAlreadyExist(AlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(InvalidPasswordLengthException.class)
    public ResponseEntity<?> handleInvalidPasswordLength(InvalidPasswordLengthException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NotExistsException.class)
    public ResponseEntity<?> handleNotExists(NotExistsException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

}
