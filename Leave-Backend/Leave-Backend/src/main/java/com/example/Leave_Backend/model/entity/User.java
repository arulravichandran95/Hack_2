package com.example.Leave_Backend.model.entity; // Declares this class belongs to the model entity package

import com.example.Leave_Backend.enums.Role; // Imports the Role enum (EMPLOYEE, MANAGER, ADMIN) for the user's role field
import jakarta.persistence.*; // Imports all JPA annotations (@Entity, @Table, @Column, @Id, @GeneratedValue, @Enumerated)
import java.time.LocalDateTime; // Imports LocalDateTime for storing the account creation timestamp
import org.hibernate.annotations.CreationTimestamp; // Imports Hibernate annotation that auto-populates the createdAt field on insert

/**
 * User entity representing an employee in the system.
 */
@Entity // Marks this class as a JPA entity, meaning it maps to a database table
@Table(name = "users") // Maps this entity to the "users" table in the database
public class User { // Represents a registered user (employee, manager, or admin) in the system

    @Id // Marks this field as the primary key of the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID using the database's auto-increment feature
    private Long id; // Unique identifier for each user record in the database

    @Column(nullable = false) // Maps to a DB column that does not allow null values
    private String firstName; // Stores the user's first name

    @Column(nullable = false) // Maps to a DB column that does not allow null values
    private String lastName; // Stores the user's last name

    @Column(unique = true, nullable = false) // Maps to a unique, non-null DB column (no two users can share an email)
    private String email; // Stores the user's email address, used as the login identifier

    @Column(nullable = false) // Maps to a non-null DB column
    private String password; // Stores the BCrypt-hashed password of the user (never plaintext)

    @Enumerated(EnumType.STRING) // Stores the enum value as a string ("EMPLOYEE", "ADMIN") instead of an integer in DB
    @Column(nullable = false) // Ensures role is always present in the database
    private Role role; // The role assigned to this user (EMPLOYEE, MANAGER, or ADMIN)

    private String department; // Stores the department the user belongs to (optional; nullable)

    @CreationTimestamp // Tells Hibernate to automatically set this field to the current timestamp when the entity is first persisted
    private LocalDateTime createdAt; // Records when the user account was created; auto-populated by Hibernate

    public User() {} // Default no-argument constructor required by JPA specification

    public User(Long id, String firstName, String lastName, String email, String password, Role role, String department) { // Parameterized constructor for creating a fully initialized User object
        this.id = id; // Sets the user ID
        this.firstName = firstName; // Sets the first name
        this.lastName = lastName; // Sets the last name
        this.email = email; // Sets the email address
        this.password = password; // Sets the (already hashed) password
        this.role = role; // Sets the user's role in the system
        this.department = department; // Sets the department the user belongs to
    }

    // Getters
    public Long getId() { return id; } // Returns the user's unique database ID
    public String getFirstName() { return firstName; } // Returns the user's first name
    public String getLastName() { return lastName; } // Returns the user's last name
    public String getEmail() { return email; } // Returns the user's email address
    public String getPassword() { return password; } // Returns the user's hashed password
    public Role getRole() { return role; } // Returns the user's assigned role (EMPLOYEE/MANAGER/ADMIN)
    public String getDepartment() { return department; } // Returns the department name the user belongs to
    public LocalDateTime getCreatedAt() { return createdAt; } // Returns when the user account was created

    // Setters
    public void setId(Long id) { this.id = id; } // Sets/updates the user's ID
    public void setFirstName(String firstName) { this.firstName = firstName; } // Sets/updates the user's first name
    public void setLastName(String lastName) { this.lastName = lastName; } // Sets/updates the user's last name
    public void setEmail(String email) { this.email = email; } // Sets/updates the user's email address
    public void setPassword(String password) { this.password = password; } // Sets/updates the user's hashed password
    public void setRole(Role role) { this.role = role; } // Sets/updates the user's role
    public void setDepartment(String department) { this.department = department; } // Sets/updates the user's department

    // Manual Builder Replacement (Simplified)
    public static UserBuilder builder() { // Static factory method to start building a User object fluently
        return new UserBuilder(); // Returns a new builder instance for constructing a User step-by-step
    }

    public static class UserBuilder { // Inner builder class that provides a fluent API for constructing User objects
        private User user = new User(); // Creates a blank User instance to populate through builder methods
        public UserBuilder firstName(String f) { user.setFirstName(f); return this; } // Sets first name and returns the builder for chaining
        public UserBuilder lastName(String l) { user.setLastName(l); return this; } // Sets last name and returns the builder for chaining
        public UserBuilder email(String e) { user.setEmail(e); return this; } // Sets email and returns the builder for chaining
        public UserBuilder password(String p) { user.setPassword(p); return this; } // Sets password and returns the builder for chaining
        public UserBuilder role(Role r) { user.setRole(r); return this; } // Sets role and returns the builder for chaining
        public UserBuilder department(String d) { user.setDepartment(d); return this; } // Sets department and returns the builder for chaining
        public User build() { return user; } // Finalizes and returns the fully constructed User object
    }
}
