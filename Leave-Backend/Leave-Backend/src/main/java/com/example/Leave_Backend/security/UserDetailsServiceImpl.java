package com.example.Leave_Backend.security; // Declares this class belongs to the security package

import com.example.Leave_Backend.model.entity.User; // Imports the User entity to retrieve role and credentials
import com.example.Leave_Backend.repository.UserRepository; // Imports the repository to query users from the database
import org.springframework.beans.factory.annotation.Autowired; // Enables automatic dependency injection by Spring
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Used to create a Spring Security role/authority object from a string
import org.springframework.security.core.userdetails.UserDetails; // Interface that Spring Security uses to store user authentication information
import org.springframework.security.core.userdetails.UserDetailsService; // Contract that Spring Security calls to load user by username during authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Exception to throw when a user is not found in the database
import org.springframework.stereotype.Service; // Marks this class as a Spring service bean

import java.util.Collections; // Utility to create a single-element immutable list for authorities

@Service // Registers this class as a Spring-managed service bean; used by Spring Security's authentication flow
public class UserDetailsServiceImpl implements UserDetailsService { // Implements Spring Security's user loading interface used during login/auth

    @Autowired // Injects the UserRepository to look up users from the database
    private UserRepository userRepository; // Repository used to fetch a User entity by their email address

    @Override // Overrides the method required by the UserDetailsService interface
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // Called by Spring Security with the username (email) to authenticate
        User user = userRepository.findByEmail(email) // Queries the database for a user with the given email
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email)); // Throws exception if no user exists with that email

        return new org.springframework.security.core.userdetails.User( // Builds and returns Spring Security's UserDetails object
                user.getEmail(), // Sets the username (identifier) to the user's email address
                user.getPassword(), // Sets the hashed password that Spring Security will compare during authentication
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())) // Wraps the user's role (e.g., ROLE_ADMIN) as a granted authority for Spring Security
        );
    }
}
