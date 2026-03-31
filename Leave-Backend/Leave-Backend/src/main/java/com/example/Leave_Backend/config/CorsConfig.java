package com.example.Leave_Backend.config; // Declares this class belongs to the config package

import org.springframework.context.annotation.Bean; // Imports annotation to declare this method produces a Spring-managed bean
import org.springframework.context.annotation.Configuration; // Marks this class as a source of Spring bean definitions
import org.springframework.web.servlet.config.annotation.CorsRegistry; // Provides API to configure CORS mappings for the application
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; // Interface to customize Spring MVC configuration

@Configuration // Tells Spring to treat this class as a configuration class; beans defined here are loaded at startup
public class CorsConfig { // Configuration class that defines Cross-Origin Resource Sharing (CORS) rules for the API

    @Bean // Declares this method's return value as a Spring bean managed by the application context
    public WebMvcConfigurer corsConfigurer() { // Creates a WebMvcConfigurer bean that customizes CORS settings globally
        return new WebMvcConfigurer() { // Returns an anonymous implementation of WebMvcConfigurer
            @Override // Overrides the default empty implementation to add custom CORS mappings
            public void addCorsMappings(CorsRegistry registry) { // Called by Spring MVC to configure CORS rules
                registry.addMapping("/**") // Applies CORS rules to ALL endpoints in the application (every URL path)
                        .allowedOrigins("http://localhost:3000") // Allows requests only from the React frontend running on localhost:3000
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Specifies the HTTP methods the frontend is allowed to use
                        .allowedHeaders("*") // Allows all HTTP headers in the cross-origin requests (including Authorization)
                        .allowCredentials(true); // Allows cookies and Authorization headers to be sent with cross-origin requests
            }
        };
    }
}
