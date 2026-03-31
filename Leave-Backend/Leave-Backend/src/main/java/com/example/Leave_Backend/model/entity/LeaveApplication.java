package com.example.Leave_Backend.model.entity; // Declares this class belongs to the model entity package

import com.example.Leave_Backend.enums.LeaveType; // Imports the LeaveType enum for categorizing the leave
import com.example.Leave_Backend.enums.LeaveStatus; // Imports the LeaveStatus enum for tracking approval state
import jakarta.persistence.*; // Imports all JPA annotations needed for ORM mapping
import java.time.LocalDate; // Imports LocalDate for storing start/end dates of the leave
import java.time.LocalDateTime; // Imports LocalDateTime for storing exact timestamps (applied, reviewed)

/**
 * LeaveApplication entity representing an application in the system.
 */
@Entity // Marks this class as a JPA entity mapped to a database table
@Table(name = "leave_applications") // Maps this entity to the "leave_applications" table in the database
public class LeaveApplication { // Represents a single leave application submitted by an employee

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID using the DB's identity column strategy
    private Long id; // Unique identifier for each leave application record

    @ManyToOne(fetch = FetchType.LAZY) // Many leave applications can belong to one user; loaded lazily for performance
    @JoinColumn(name = "user_id", nullable = false) // Maps the foreign key column "user_id" in the DB; cannot be null
    private User user; // The employee who submitted this leave application

    @Enumerated(EnumType.STRING) // Stores the enum as a readable string in the DB (e.g., "ANNUAL")
    @Column(nullable = false) // Ensures leave type is always present
    private LeaveType leaveType; // The category of leave being requested (ANNUAL, SICK, CASUAL, etc.)

    @Column(nullable = false) // Maps to a non-null DB column
    private LocalDate startDate; // The first day of the requested leave period

    @Column(nullable = false) // Maps to a non-null DB column
    private LocalDate endDate; // The last day of the requested leave period

    @Column(nullable = false) // Maps to a non-null DB column
    private Integer totalDays; // The number of working days within the leave period (calculated, not raw calendar days)

    @Column(nullable = false) // Maps to a non-null DB column
    private String reason; // The reason provided by the employee for taking the leave

    @Enumerated(EnumType.STRING) // Stores enum status as string (e.g., "PENDING") in the DB
    @Column(nullable = false) // Ensures status is always present in each record
    private LeaveStatus status; // Current approval status of the leave (PENDING, APPROVED, REJECTED)

    @Column(nullable = false) // Maps to a non-null DB column
    private LocalDateTime appliedAt; // Timestamp recording when the employee submitted the leave application

    private LocalDateTime reviewedAt; // Timestamp of when an admin/manager reviewed the application (nullable until reviewed)

    private String reviewedBy; // Name/email of the admin or manager who reviewed this application (nullable)

    private String comments; // Optional comments added by the reviewer during approval or rejection

    public LeaveApplication() {} // Default no-argument constructor required by JPA

    public LeaveApplication(Long id, User user, LeaveType type, LocalDate start, LocalDate end, Integer totalDays, String reason, LeaveStatus status, LocalDateTime appliedAt) { // Parameterized constructor for building a complete leave application
        this.id = id; // Sets the unique ID
        this.user = user; // Sets the associated employee
        this.leaveType = type; // Sets the leave category
        this.startDate = start; // Sets the start date
        this.endDate = end; // Sets the end date
        this.totalDays = totalDays; // Sets the total working days count
        this.reason = reason; // Sets the reason for leave
        this.status = status; // Sets the initial status (typically PENDING)
        this.appliedAt = appliedAt; // Sets the timestamp of when the application was created
    }

    // Getters
    public Long getId() { return id; } // Returns the leave application's unique ID
    public User getUser() { return user; } // Returns the employee who applied for this leave
    public LeaveType getLeaveType() { return leaveType; } // Returns the type of leave applied for
    public LocalDate getStartDate() { return startDate; } // Returns the start date of the leave
    public LocalDate getEndDate() { return endDate; } // Returns the end date of the leave
    public Integer getTotalDays() { return totalDays; } // Returns the total working days requested
    public String getReason() { return reason; } // Returns the reason given for the leave
    public LeaveStatus getStatus() { return status; } // Returns the current status of the application
    public LocalDateTime getAppliedAt() { return appliedAt; } // Returns when the application was submitted
    public LocalDateTime getReviewedAt() { return reviewedAt; } // Returns when the application was reviewed (null if pending)
    public String getReviewedBy() { return reviewedBy; } // Returns who reviewed the application (null if pending)
    public String getComments() { return comments; } // Returns reviewer's comments on the application (null if none)

    // Setters
    public void setId(Long id) { this.id = id; } // Sets the unique ID
    public void setUser(User user) { this.user = user; } // Sets the associated employee
    public void setLeaveType(LeaveType leaveType) { this.leaveType = leaveType; } // Sets the leave category
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; } // Sets the leave start date
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; } // Sets the leave end date
    public void setTotalDays(Integer totalDays) { this.totalDays = totalDays; } // Sets the total working days count
    public void setReason(String reason) { this.reason = reason; } // Sets the reason for leave
    public void setStatus(LeaveStatus status) { this.status = status; } // Updates the status (e.g., from PENDING to APPROVED)
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; } // Sets the application submission timestamp
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; } // Sets the timestamp when the application was reviewed
    public void setReviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; } // Sets who reviewed the application
    public void setComments(String comments) { this.comments = comments; } // Sets the reviewer's comment on the decision

    // Manual Builder
    public static LeaveApplicationBuilder builder() { // Static factory to begin building a LeaveApplication fluently
        return new LeaveApplicationBuilder(); // Returns a blank builder instance
    }

    public static class LeaveApplicationBuilder { // Inner builder class providing a fluent API for constructing LeaveApplication objects
        private LeaveApplication app = new LeaveApplication(); // Creates a blank LeaveApplication to fill during building
        public LeaveApplicationBuilder user(User u) { app.setUser(u); return this; } // Sets the user and chains the builder
        public LeaveApplicationBuilder leaveType(LeaveType t) { app.setLeaveType(t); return this; } // Sets the leave type and chains
        public LeaveApplicationBuilder startDate(LocalDate s) { app.setStartDate(s); return this; } // Sets the start date and chains
        public LeaveApplicationBuilder endDate(LocalDate e) { app.setEndDate(e); return this; } // Sets the end date and chains
        public LeaveApplicationBuilder totalDays(Integer d) { app.setTotalDays(d); return this; } // Sets total working days and chains
        public LeaveApplicationBuilder reason(String r) { app.setReason(r); return this; } // Sets the leave reason and chains
        public LeaveApplicationBuilder status(LeaveStatus s) { app.setStatus(s); return this; } // Sets the status and chains
        public LeaveApplicationBuilder appliedAt(LocalDateTime a) { app.setAppliedAt(a); return this; } // Sets the applied timestamp and chains
        public LeaveApplication build() { return app; } // Returns the final fully-built LeaveApplication object
    }
}
