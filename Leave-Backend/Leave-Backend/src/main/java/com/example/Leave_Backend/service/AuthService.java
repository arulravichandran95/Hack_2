package com.example.Leave_Backend.service;

import com.example.Leave_Backend.model.dto.LoginRequest;
import com.example.Leave_Backend.model.dto.LoginResponse;
import com.example.Leave_Backend.model.dto.RegisterRequest;
import com.example.Leave_Backend.model.entity.User;

public interface AuthService {
    User register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
}
