package com.example.Leave_Backend.exception; // Declares this class belongs to the exception package

import org.springframework.http.HttpStatus; // Imports HTTP status codes used in the response
import org.springframework.web.bind.annotation.ResponseStatus; // Annotation to automatically set a specific HTTP status on exceptions

@ResponseStatus(HttpStatus.NOT_FOUND) // Tells Spring to return HTTP 404 whenever this exception is thrown
public class ResourceNotFoundException extends RuntimeException { // Custom unchecked exception indicating a requested resource was not found in the database
    public ResourceNotFoundException(String message) { // Constructor that accepts a descriptive error message
        super(message); // Passes the message to RuntimeException so it appears in the error response
    }
}
