package com.example.Leave_Backend.model.dto;

import com.example.Leave_Backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private Long id;
    private String email;
    private Role role;
}
