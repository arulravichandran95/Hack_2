package com.example.Leave_Backend; // Declares the root package for this Spring Boot application

import org.springframework.boot.SpringApplication; // Import to bootstrap and launch a Spring application
import org.springframework.boot.autoconfigure.SpringBootApplication; // Imports the annotation that enables auto-configuration, component scanning, and configuration

@SpringBootApplication // Marks this as the main Spring Boot application class; enables component scan, auto-config, and property support
public class LeaveBackendApplication { // Main class that serves as the entry point for the entire Spring Boot application

	public static void main(String[] args) { // Standard Java main method; entry point when the JAR/application is run
		SpringApplication.run(LeaveBackendApplication.class, args); // Launches the Spring Boot application and creates the application context
	}

}
