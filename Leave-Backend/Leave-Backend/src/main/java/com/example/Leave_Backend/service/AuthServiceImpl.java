package com.example.Leave_Backend.service; // Declares this class belongs to the service package

import com.example.Leave_Backend.enums.Role; // Imports the Role enum to default unspecified roles to EMPLOYEE
import com.example.Leave_Backend.model.dto.LoginRequest; // Imports the login request DTO with email and password
import com.example.Leave_Backend.model.dto.LoginResponse; // Imports the login response DTO containing the JWT and user info
import com.example.Leave_Backend.model.dto.RegisterRequest; // Imports the registration request DTO
import com.example.Leave_Backend.model.entity.LeaveBalance; // Imports the LeaveBalance entity to create default balances on registration
import com.example.Leave_Backend.model.entity.User; // Imports the User entity for creation and lookup
import com.example.Leave_Backend.repository.LeaveBalanceRepository; // Imports repository to persist the new user's leave balance
import com.example.Leave_Backend.repository.UserRepository; // Imports repository to check email uniqueness and save the user
import com.example.Leave_Backend.security.JwtTokenProvider; // Imports the JWT provider to generate a token after login
import org.springframework.beans.factory.annotation.Autowired; // Enables field-level dependency injection
import org.springframework.security.authentication.AuthenticationManager; // Manages authentication by delegating to an AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Wraps credentials for the authentication manager to verify
import org.springframework.security.core.Authentication; // Holds the result of a successful authentication
import org.springframework.security.crypto.password.PasswordEncoder; // Encodes passwords before storing them in the database
import org.springframework.stereotype.Service; // Marks this class as a Spring service bean
import org.springframework.transaction.annotation.Transactional; // Ensures operations are wrapped in a database transaction

import java.time.Year; // Imports Year to record the current year for the initial leave balance

@Service // Registers this class as a Spring-managed service bean
public class AuthServiceImpl implements AuthService { // Implements the AuthService interface for authentication business logic

    @Autowired // Injects the UserRepository for database operations on users
    private UserRepository userRepository; // Used to check email uniqueness and save the newly registered user

    @Autowired // Injects the LeaveBalanceRepository for creating default leave balances
    private LeaveBalanceRepository leaveBalanceRepository; // Used to save the initial leave balance after registering a new user

    @Autowired // Injects the PasswordEncoder for hashing passwords
    private PasswordEncoder passwordEncoder; // BCrypt encoder used to hash the user's password before saving to DB

    @Autowired // Injects the AuthenticationManager for verifying login credentials
    private AuthenticationManager authenticationManager; // Used to authenticate username/password during login; delegates to UserDetailsService

    @Autowired // Injects the JWT token provider
    private JwtTokenProvider tokenProvider; // Used to generate a JWT after successful authentication

    @Override // Overrides the register method from the AuthService interface
    @Transactional // Wraps the method in a database transaction; rolls back if any error occurs
    public User register(RegisterRequest registerRequest) { // Handles the full user registration flow
        if (userRepository.existsByEmail(registerRequest.getEmail())) { // Checks if a user with the same email already exists
            throw new RuntimeException("Email already in use"); // Throws an error to prevent duplicate registrations
        }

        User user = User.builder() // Starts building the new User entity using the fluent builder
                .firstName(registerRequest.getFirstName()) // Sets first name from the registration form
                .lastName(registerRequest.getLastName()) // Sets last name from the registration form
                .email(registerRequest.getEmail()) // Sets email from the registration form
                .password(passwordEncoder.encode(registerRequest.getPassword())) // Hashes the user's raw password using BCrypt before storage
                .role(registerRequest.getRole() != null ? registerRequest.getRole() : Role.EMPLOYEE) // Uses the requested role, or defaults to EMPLOYEE if none provided
                .department(registerRequest.getDepartment()) // Sets the department from the registration form
                .build(); // Creates the final User object

        User savedUser = userRepository.save(user); // Persists the new user to the database and retrieves the saved entity with its generated ID

        // Assign default leave balance: 12 annual, 10 sick, 6 casual
        LeaveBalance leaveBalance = LeaveBalance.builder() // Starts building the initial leave balance for the new user
                .user(savedUser) // Links the leave balance to the newly created user
                .annualLeave(12) // Assigns 12 days of annual leave as the starting balance
                .sickLeave(10) // Assigns 10 days of sick leave as the starting balance
                .casualLeave(6) // Assigns 6 days of casual leave as the starting balance
                .year(Year.now().getValue()) // Sets the current calendar year for this balance record
                .build(); // Builds the LeaveBalance object

        leaveBalanceRepository.save(leaveBalance); // Saves the default leave balance to the database

        return savedUser; // Returns the saved user entity (without the hashed password visible on frontend)
    }

    @Override // Overrides the login method from the AuthService interface
    public LoginResponse login(LoginRequest loginRequest) { // Handles the user login flow
        Authentication authentication = authenticationManager.authenticate( // Asks Spring Security to authenticate the given credentials
                new UsernamePasswordAuthenticationToken( // Wraps the email and password into an authentication token
                        loginRequest.getEmail(), // The email entered by the user
                        loginRequest.getPassword() // The raw password entered by the user
                )
        ); // If credentials are wrong, an exception is thrown automatically by Spring Security

        String token = tokenProvider.generateToken(authentication); // Generates a signed JWT token for the authenticated user
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(); // Fetches the full User entity from DB to include in the response

        return LoginResponse.builder() // Starts building the login response object
                .token(token) // Includes the JWT token so the client can use it for future requests
                .id(user.getId()) // Includes the user's DB ID
                .email(user.getEmail()) // Includes the user's email address
                .role(user.getRole()) // Includes the user's role (EMPLOYEE/MANAGER/ADMIN) for frontend routing
                .build(); // Returns the final login response
    }
}
