package com.example.Leave_Backend.repository; // Declares this interface belongs to the repository package

import com.example.Leave_Backend.model.entity.User; // Imports the User entity this repository manages
import org.springframework.data.jpa.repository.JpaRepository; // Imports the base JPA repository with built-in CRUD operations
import org.springframework.stereotype.Repository; // Marks this as a Spring Data repository component

import java.util.Optional; // Imports Optional for safe null handling when a user may not be found

/**
 * Repository interface for User entity.
 */
@Repository // Tells Spring to auto-detect this as a repository bean and handle persistence exceptions
public interface UserRepository extends JpaRepository<User, Long> { // Extends JpaRepository to inherit CRUD operations for User entity with Long as the ID type
    Optional<User> findByEmail(String email); // Spring Data automatically generates a query to find a user by their email address; returns Optional to avoid null
    Boolean existsByEmail(String email); // Spring Data generates a query to check if a user with the given email already exists; used during registration to prevent duplicate accounts
}
