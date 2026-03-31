package com.example.Leave_Backend.model.dto; // Declares this class belongs to the model DTO package

import com.example.Leave_Backend.enums.Role; // Imports the Role enum so users can optionally specify a role during registration
import jakarta.validation.constraints.Email; // Validates that the email field is in a valid email format
import jakarta.validation.constraints.NotBlank; // Validates that string fields are not null or blank
import jakarta.validation.constraints.Size; // Validates the length of a string field

/**
 * DTO for register request.
 */
public class RegisterRequest { // Data Transfer Object used to receive new user registration data from the client

    @NotBlank(message = "First name is required") // Validation: rejects the request if firstName is missing or blank
    private String firstName; // The first name of the new user being registered

    @NotBlank(message = "Last name is required") // Validation: rejects the request if lastName is missing or blank
    private String lastName; // The last name of the new user being registered

    @NotBlank(message = "Email is required") // Validation: rejects the request if email is missing or blank
    @Email(message = "Email should be valid") // Validation: rejects the request if the email is not properly formatted
    private String email; // The unique email address for the new user's account

    @NotBlank(message = "Password is required") // Validation: rejects the request if password is missing or blank
    @Size(min = 6, message = "Password should be at least 6 characters") // Validation: rejects passwords shorter than 6 characters
    private String password; // The plain-text password for the new user; will be hashed with BCrypt before saving

    private Role role; // Optional role for the new user; defaults to EMPLOYEE in the service if not provided
    private String department; // Optional department the user belongs to (e.g., "Engineering", "HR")

    public RegisterRequest() {} // Default no-argument constructor required for Jackson JSON deserialization

    public RegisterRequest(String firstName, String lastName, String email, String password, Role role, String department) { // Parameterized constructor for creating a registration request programmatically
        this.firstName = firstName; // Sets the first name
        this.lastName = lastName; // Sets the last name
        this.email = email; // Sets the email
        this.password = password; // Sets the raw password
        this.role = role; // Sets the optional role
        this.department = department; // Sets the optional department
    }

    // Getters
    public String getFirstName() { return firstName; } // Returns the first name from the registration request
    public String getLastName() { return lastName; } // Returns the last name from the registration request
    public String getEmail() { return email; } // Returns the email from the registration request
    public String getPassword() { return password; } // Returns the plaintext password from the registration request
    public Role getRole() { return role; } // Returns the requested role (may be null; defaults to EMPLOYEE)
    public String getDepartment() { return department; } // Returns the department from the registration request

    // Setters
    public void setFirstName(String firstName) { this.firstName = firstName; } // Sets the first name
    public void setLastName(String lastName) { this.lastName = lastName; } // Sets the last name
    public void setEmail(String email) { this.email = email; } // Sets the email
    public void setPassword(String password) { this.password = password; } // Sets the password
    public void setRole(Role role) { this.role = role; } // Sets the role
    public void setDepartment(String department) { this.department = department; } // Sets the department
}
