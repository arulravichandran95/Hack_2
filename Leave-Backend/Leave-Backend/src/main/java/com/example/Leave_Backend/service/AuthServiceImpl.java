package com.example.Leave_Backend.service;

import com.example.Leave_Backend.enums.Role;
import com.example.Leave_Backend.model.dto.LoginRequest;
import com.example.Leave_Backend.model.dto.LoginResponse;
import com.example.Leave_Backend.model.dto.RegisterRequest;
import com.example.Leave_Backend.model.entity.LeaveBalance;
import com.example.Leave_Backend.model.entity.User;
import com.example.Leave_Backend.repository.LeaveBalanceRepository;
import com.example.Leave_Backend.repository.UserRepository;
import com.example.Leave_Backend.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    @Transactional
    public User register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole() != null ? registerRequest.getRole() : Role.EMPLOYEE)
                .department(registerRequest.getDepartment())
                .build();

        User savedUser = userRepository.save(user);

        // Assign default leave balance: 12 annual, 10 sick, 6 casual
        LeaveBalance leaveBalance = LeaveBalance.builder()
                .user(savedUser)
                .annualLeave(12)
                .sickLeave(10)
                .casualLeave(6)
                .year(Year.now().getValue())
                .build();

        leaveBalanceRepository.save(leaveBalance);

        return savedUser;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        String token = tokenProvider.generateToken(authentication);
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();

        return LoginResponse.builder()
                .token(token)
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
