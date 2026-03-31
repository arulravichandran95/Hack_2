package com.example.Leave_Backend.security; // Declares this class belongs to the security package

import jakarta.servlet.FilterChain; // Imports FilterChain to pass the request to the next filter in the chain
import jakarta.servlet.ServletException; // Imports exception type thrown by servlet operations
import jakarta.servlet.http.HttpServletRequest; // Imports the HTTP request object to read headers
import jakarta.servlet.http.HttpServletResponse; // Imports the HTTP response object to send back responses
import org.springframework.beans.factory.annotation.Autowired; // Enables automatic dependency injection
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Creates an authentication token with user details and authorities
import org.springframework.security.core.context.SecurityContextHolder; // Holds the current security context (authenticated user) for this request
import org.springframework.security.core.userdetails.UserDetails; // Interface representing the authenticated user's details
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // Builds request-specific authentication details (IP, session ID)
import org.springframework.stereotype.Component; // Registers this class as a Spring-managed component bean
import org.springframework.util.StringUtils; // Utility to check if a string has text content
import org.springframework.web.filter.OncePerRequestFilter; // Base class ensuring this filter is only executed once per HTTP request

import java.io.IOException; // Imports IOException for I/O error handling during filter execution

@Component // Marks this class as a Spring bean so it is auto-detected and instantiated
public class JwtAuthFilter extends OncePerRequestFilter { // Custom security filter that intercepts every HTTP request exactly once to validate JWT

    @Autowired // Injects the JwtTokenProvider bean for token parsing and validation
    private JwtTokenProvider tokenProvider; // Used to validate the JWT and extract the username from it

    @Autowired // Injects the UserDetailsServiceImpl bean for loading user details from DB
    private UserDetailsServiceImpl userDetailsService; // Used to load the full UserDetails object by email

    @Override // Overrides the template method from OncePerRequestFilter
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException { // Main filter logic executed for every incoming HTTP request
        try {
            String jwt = getJwtFromRequest(request); // Extracts the JWT token string from the Authorization header

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) { // Checks if the token is present and valid
                String username = tokenProvider.getUsernameFromToken(jwt); // Extracts the email/username from the validated token

                UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Loads the full UserDetails (with roles/authorities) from database
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()); // Creates an authentication object with user info and their granted roles
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Attaches request-specific details (IP address, session) to the authentication

                SecurityContextHolder.getContext().setAuthentication(authentication); // Sets the authenticated user in the security context for this request thread
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex); // Logs error without stopping the filter chain; allows request to continue unauthenticated
        }

        filterChain.doFilter(request, response); // Passes the request and response to the next filter in the security chain
    }

    private String getJwtFromRequest(HttpServletRequest request) { // Helper method to extract the JWT string from the HTTP Authorization header
        String bearerToken = request.getHeader("Authorization"); // Reads the 'Authorization' header value from the incoming HTTP request
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) { // Checks if the header is present and follows the "Bearer <token>" format
            return bearerToken.substring(7); // Strips the "Bearer " prefix (7 characters) and returns just the raw JWT string
        }
        return null; // Returns null if the Authorization header is missing or malformed
    }
}
