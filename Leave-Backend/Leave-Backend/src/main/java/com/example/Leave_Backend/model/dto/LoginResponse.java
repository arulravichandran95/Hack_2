package com.example.Leave_Backend.model.dto; // Declares this class belongs to the model DTO package

import com.example.Leave_Backend.enums.Role; // Imports the Role enum so the response includes the user's role

/**
 * DTO for login response.
 */
public class LoginResponse { // Data Transfer Object sent back to the client after a successful login, containing JWT and user info
    private String token; // The generated JWT token the client must send in the Authorization header for all future requests
    private Long id; // The unique database ID of the authenticated user
    private String email; // The email address of the authenticated user
    private Role role; // The role of the authenticated user (EMPLOYEE, MANAGER, or ADMIN)

    public LoginResponse() {} // Default no-argument constructor required for Jackson JSON serialization

    public LoginResponse(String token, Long id, String email, Role role) { // Constructor to build a complete login response
        this.token = token; // Sets the JWT token
        this.id = id; // Sets the user ID
        this.email = email; // Sets the email
        this.role = role; // Sets the user role
    }

    // Getters
    public String getToken() { return token; } // Returns the JWT token string to send to the client
    public Long getId() { return id; } // Returns the user's database ID
    public String getEmail() { return email; } // Returns the authenticated user's email
    public Role getRole() { return role; } // Returns the user's role (EMPLOYEE/MANAGER/ADMIN)

    // Setters
    public void setToken(String token) { this.token = token; } // Sets the JWT token
    public void setId(Long id) { this.id = id; } // Sets the user ID
    public void setEmail(String email) { this.email = email; } // Sets the email
    public void setRole(Role role) { this.role = role; } // Sets the user role

    // Manual Builder
    public static LoginResponseBuilder builder() { // Static factory to start building a LoginResponse fluently
        return new LoginResponseBuilder(); // Returns a new builder instance
    }

    public static class LoginResponseBuilder { // Inner builder class providing a fluent API for creating LoginResponse objects
        private LoginResponse response = new LoginResponse(); // Creates a blank response as the building target
        public LoginResponseBuilder token(String t) { response.setToken(t); return this; } // Sets the token and returns the builder for chaining
        public LoginResponseBuilder id(Long i) { response.setId(i); return this; } // Sets the user ID and returns the builder
        public LoginResponseBuilder email(String e) { response.setEmail(e); return this; } // Sets the email and returns the builder
        public LoginResponseBuilder role(Role r) { response.setRole(r); return this; } // Sets the role and returns the builder
        public LoginResponse build() { return response; } // Returns the final constructed LoginResponse object
    }
}
