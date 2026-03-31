package com.example.Leave_Backend.config; // Declares this class belongs to the config package

import com.example.Leave_Backend.security.JwtAuthFilter; // Imports the custom JWT filter that intercepts requests before authentication
import com.example.Leave_Backend.security.UserDetailsServiceImpl; // Imports our custom UserDetailsService that loads users from DB
import org.springframework.context.annotation.Bean; // Marks methods that produce Spring-managed beans
import org.springframework.context.annotation.Configuration; // Marks this as a configuration class with bean definitions
import org.springframework.security.authentication.AuthenticationManager; // Interface used to authenticate user credentials
import org.springframework.security.authentication.AuthenticationProvider; // Interface that performs the actual authentication logic
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // DAO-based implementation of AuthenticationProvider using UserDetailsService
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Provides the AuthenticationManager configured from the application context
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Enables method-level security annotations like @PreAuthorize
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Builder to configure HTTP security rules for the application
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Enables Spring Security's web security support
import org.springframework.security.config.http.SessionCreationPolicy; // Enum to specify how sessions are managed (STATELESS for JWT apps)
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Encoder that hashes passwords using the BCrypt algorithm
import org.springframework.security.crypto.password.PasswordEncoder; // Interface abstraction for password encoding
import org.springframework.security.web.SecurityFilterChain; // Defines the chain of security filters applied to HTTP requests
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Standard filter for username/password authentication; our JWT filter runs before this

/**
 * Security configuration for the application.
 */
@Configuration // Declares this class as a Spring configuration; all @Bean methods are registered
@EnableWebSecurity // Activates Spring Security for web requests and overrides default settings
@EnableMethodSecurity // Enables @PreAuthorize and @PostAuthorize annotations on controller methods
public class SecurityConfig { // Main security configuration class for the entire application

    private final UserDetailsServiceImpl userDetailsService; // Our custom service that loads user data from the database
    private final JwtAuthFilter jwtAuthFilter; // Our custom JWT filter that runs before each request to authenticate via token

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, JwtAuthFilter jwtAuthFilter) { // Constructor injection of dependencies (preferred over @Autowired)
        this.userDetailsService = userDetailsService; // Stores the user details service for use in authentication provider
        this.jwtAuthFilter = jwtAuthFilter; // Stores the JWT filter reference for registration in filter chain
    }

    @Bean // Exposes this PasswordEncoder as a Spring bean so it can be injected anywhere
    public PasswordEncoder passwordEncoder() { // Bean method that provides BCrypt password hashing
        return new BCryptPasswordEncoder(); // Returns a BCryptPasswordEncoder instance with default strength (10 rounds)
    }

    @Bean // Exposes the AuthenticationManager as a bean needed for manual auth (e.g., login endpoint)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { // Retrieves the AuthenticationManager from Spring Security's auto-configured setup
        return config.getAuthenticationManager(); // Returns the globally configured AuthenticationManager
    }

    @Bean // Declares the AuthenticationProvider configuration as a Spring bean
    public AuthenticationProvider authenticationProvider() { // Creates a DAO-based authentication provider using our UserDetailsService
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); // Creates a provider that loads users from DB and compares passwords
        authProvider.setUserDetailsService(userDetailsService); // Tells Spring Security to use our custom UserDetailsService to load users
        authProvider.setPasswordEncoder(passwordEncoder()); // Sets the BCrypt encoder so Spring can verify hashed passwords
        return authProvider; // Returns the fully configured authentication provider
    }

    @Bean // Declares the SecurityFilterChain as a bean; this replaces WebSecurityConfigurerAdapter configuration
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // Configures the full HTTP security rules for the API
        http
            .csrf(csrf -> csrf.disable()) // Disables CSRF protection since the app uses stateless JWT tokens instead of sessions
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configures the app to never create HTTP sessions; each request must carry a JWT
            .authorizeHttpRequests(auth -> auth // Begins defining URL-level authorization rules
                .requestMatchers("/api/auth/**").permitAll() // Allows unauthenticated access to all authentication endpoints (login, register)
                .requestMatchers("/api/admin/**").hasAnyRole("ADMIN", "MANAGER") // Restricts admin endpoints to only ADMIN and MANAGER roles
                .anyRequest().authenticated() // Requires authentication for all other requests not explicitly permitted
            )
            .authenticationProvider(authenticationProvider()) // Registers our custom DAO authentication provider for credential validation
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Adds our JWT filter to run BEFORE Spring's default username/password filter

        return http.build(); // Builds and returns the finalized security filter chain configuration
    }
}
