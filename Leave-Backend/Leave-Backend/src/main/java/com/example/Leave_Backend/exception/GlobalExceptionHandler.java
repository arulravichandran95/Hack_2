package com.example.Leave_Backend.exception; // Declares this class belongs to the exception package

import org.springframework.http.HttpStatus; // Imports HTTP status codes (404, 400, 500) for responses
import org.springframework.http.ResponseEntity; // Wraps the response body and HTTP status code together
import org.springframework.web.bind.MethodArgumentNotValidException; // Exception thrown when @Valid validation fails on request body fields
import org.springframework.web.bind.annotation.ControllerAdvice; // Marks this class as a global exception handler for all controllers
import org.springframework.web.bind.annotation.ExceptionHandler; // Marks specific methods to handle particular exception types

import java.util.HashMap; // Used to create a map for storing error key-value pairs
import java.util.Map; // Map interface reference for error message containers

@ControllerAdvice // Makes this class a global exception handler intercepting exceptions from all @RestController classes
public class GlobalExceptionHandler { // Centralized class for turning exceptions into structured JSON error responses

    @ExceptionHandler(ResourceNotFoundException.class) // Handles all ResourceNotFoundException thrown anywhere in the application
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) { // Method invoked when a resource is not found
        Map<String, String> error = new HashMap<>(); // Creates a map to hold the error message
        error.put("message", ex.getMessage()); // Puts the exception's message into the response body under "message" key
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND); // Returns HTTP 404 with the error JSON body
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // Handles validation failures from @Valid annotated request bodies
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) { // Method invoked when a request field fails validation
        Map<String, String> errors = new HashMap<>(); // Creates a map to store all field-level validation error messages
        ex.getBindingResult().getFieldErrors().forEach(error -> // Iterates over every field that failed validation
            errors.put(error.getField(), error.getDefaultMessage())); // Maps field name -> validation error message into the errors map
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // Returns HTTP 400 with all validation error details as JSON
    }

    @ExceptionHandler(Exception.class) // Fallback handler for any unexpected exception not caught by the above handlers
    public ResponseEntity<?> handleGlobalException(Exception ex) { // Method invoked for any other unhandled runtime exception
        Map<String, String> error = new HashMap<>(); // Creates a map to hold the generic error message
        error.put("message", "An unexpected error occurred: " + ex.getMessage()); // Puts a friendly error message along with the exception detail
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR); // Returns HTTP 500 with the error message JSON body
    }
}
