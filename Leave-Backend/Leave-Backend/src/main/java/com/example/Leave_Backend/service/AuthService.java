package com.example.Leave_Backend.service; // Declares this interface belongs to the service package

import com.example.Leave_Backend.model.dto.LoginRequest; // Imports the LoginRequest DTO used as input for login
import com.example.Leave_Backend.model.dto.LoginResponse; // Imports the LoginResponse DTO returned after successful login
import com.example.Leave_Backend.model.dto.RegisterRequest; // Imports the RegisterRequest DTO used as input for registration
import com.example.Leave_Backend.model.entity.User; // Imports the User entity returned after successful registration

public interface AuthService { // Service interface defining the contract for authentication operations
    User register(RegisterRequest registerRequest); // Registers a new user and returns the saved User entity
    LoginResponse login(LoginRequest loginRequest); // Authenticates a user and returns a LoginResponse containing the JWT token and user details
}
