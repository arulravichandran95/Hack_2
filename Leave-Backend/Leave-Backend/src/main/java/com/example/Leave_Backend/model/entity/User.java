package com.example.Leave_Backend.model.entity;

import com.example.Leave_Backend.enums.Role;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

/**
 * User entity representing an employee in the system.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String department;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public User() {}

    public User(Long id, String firstName, String lastName, String email, String password, Role role, String department) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.department = department;
    }

    // Getters
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public String getDepartment() { return department; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(Role role) { this.role = role; }
    public void setDepartment(String department) { this.department = department; }

    // Manual Builder Replacement (Simplified)
    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private User user = new User();
        public UserBuilder firstName(String f) { user.setFirstName(f); return this; }
        public UserBuilder lastName(String l) { user.setLastName(l); return this; }
        public UserBuilder email(String e) { user.setEmail(e); return this; }
        public UserBuilder password(String p) { user.setPassword(p); return this; }
        public UserBuilder role(Role r) { user.setRole(r); return this; }
        public UserBuilder department(String d) { user.setDepartment(d); return this; }
        public User build() { return user; }
    }
}
