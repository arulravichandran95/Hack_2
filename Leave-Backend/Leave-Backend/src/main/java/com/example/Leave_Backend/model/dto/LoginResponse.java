package com.example.Leave_Backend.model.dto;

import com.example.Leave_Backend.enums.Role;

/**
 * DTO for login response.
 */
public class LoginResponse {
    private String token;
    private Long id;
    private String email;
    private Role role;

    public LoginResponse() {}

    public LoginResponse(String token, Long id, String email, Role role) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.role = role;
    }

    // Getters
    public String getToken() { return token; }
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }

    // Setters
    public void setToken(String token) { this.token = token; }
    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(Role role) { this.role = role; }

    // Manual Builder
    public static LoginResponseBuilder builder() {
        return new LoginResponseBuilder();
    }

    public static class LoginResponseBuilder {
        private LoginResponse response = new LoginResponse();
        public LoginResponseBuilder token(String t) { response.setToken(t); return this; }
        public LoginResponseBuilder id(Long i) { response.setId(i); return this; }
        public LoginResponseBuilder email(String e) { response.setEmail(e); return this; }
        public LoginResponseBuilder role(Role r) { response.setRole(r); return this; }
        public LoginResponse build() { return response; }
    }
}
