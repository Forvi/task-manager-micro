package ru.user.example.exception;

public class InvalidPasswordLength extends RuntimeException {
    public InvalidPasswordLength(String message) {
        super(message);
    }
}
