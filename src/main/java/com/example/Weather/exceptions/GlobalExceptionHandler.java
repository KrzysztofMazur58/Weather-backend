package com.example.Weather.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>("Nieprawidłowe parametry: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<String> handleRestClientError(RestClientException ex) {
        return new ResponseEntity<>("Błąd połączenia z API pogodowym", HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericError(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>("Wystąpił błąd serwera", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

