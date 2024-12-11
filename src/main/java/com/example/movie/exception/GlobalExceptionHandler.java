package com.example.movie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle ResourceNotFoundException and return 404 NOT FOUND status
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(
            ResourceNotFoundException ex
    ) {
        // Return the exception message with 404 NOT FOUND status
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Handle RuntimeException and return 400 BAD REQUEST status
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeException(
            RuntimeException ex
    ) {
        // Return the exception message with 400 BAD REQUEST status
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    //@ExceptionHandler(MethodArgumentNotValidException.class)
    //public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + ex.getMessage());
    //}

    // Handle validation errors, such as @Valid violations
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationException(
            MethodArgumentNotValidException ex
    ) {
        // Map to hold the validation errors for each field
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    // Extract the field name and error message from each validation error
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        // Return the validation errors with 400 BAD REQUEST status
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    
}