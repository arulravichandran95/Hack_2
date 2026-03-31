package com.example.Leave_Backend.model.dto; // Declares this class belongs to the model DTO package

import jakarta.validation.constraints.Email; // Validation annotation to verify the email field is a valid email format
import jakarta.validation.constraints.NotBlank; // Validation annotation to ensure a string field is not null or whitespace

/**
 * DTO for login request.
 */
public class LoginRequest { // Data Transfer Object used to receive login credentials from the client

    @NotBlank(message = "Email is required") // Validation: fails if email is null or empty
    @Email(message = "Email should be valid") // Validation: fails if the value is not a properly formatted email address
    private String email; // The email address entered by the user trying to log in

    @NotBlank(message = "Password is required") // Validation: fails if password is null or blank
    private String password; // The raw plaintext password entered by the user (compared against BCrypt hash in DB)

    public LoginRequest() {} // Default no-argument constructor required for Jackson JSON deserialization

    public LoginRequest(String email, String password) { // Parameterized constructor for creating LoginRequest instances directly
        this.email = email; // Sets the email field
        this.password = password; // Sets the password field
    }

    // Getters
    public String getEmail() { return email; } // Returns the email provided in the login request
    public String getPassword() { return password; } // Returns the password provided in the login request

    // Setters
    public void setEmail(String email) { this.email = email; } // Sets/updates the email field (used during deserialization)
    public void setPassword(String password) { this.password = password; } // Sets/updates the password field
}
