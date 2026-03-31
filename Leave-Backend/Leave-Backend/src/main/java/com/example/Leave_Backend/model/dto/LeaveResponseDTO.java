package com.example.Leave_Backend.model.dto; // Declares this class belongs to the model DTO package

import com.example.Leave_Backend.enums.LeaveType; // Imports the LeaveType enum for the leave category field
import com.example.Leave_Backend.enums.LeaveStatus; // Imports the LeaveStatus enum for the approval status field
import java.time.LocalDate; // Imports LocalDate for start and end date fields
import java.time.LocalDateTime; // Imports LocalDateTime for applied and reviewed timestamp fields

/**
 * DTO for leave response.
 */
public class LeaveResponseDTO { // Data Transfer Object used to send leave application details back to the client in API responses
    private Long id; // The unique database ID of the leave application
    private LeaveType leaveType; // The type of leave (ANNUAL, SICK, CASUAL, etc.)
    private LocalDate startDate; // The start date of the leave period
    private LocalDate endDate; // The end date of the leave period
    private Integer totalDays; // Total number of working days requested
    private String reason; // The reason provided by the employee
    private LeaveStatus status; // Current approval status (PENDING, APPROVED, REJECTED)
    private LocalDateTime appliedAt; // Timestamp when the application was originally submitted
    private LocalDateTime reviewedAt; // Timestamp when the application was reviewed by an admin/manager
    private String reviewedBy; // Name/email of the person who reviewed this application
    private String comments; // Optional comments from the reviewer explaining their decision

    public LeaveResponseDTO() {} // Default no-argument constructor required for Jackson JSON serialization

    public LeaveResponseDTO(Long id, LeaveType type, LocalDate start, LocalDate end, Integer totalDays, String reason, LeaveStatus status, LocalDateTime appliedAt) { // Constructor for building a partial response (without review details)
        this.id = id; // Sets the leave ID
        this.leaveType = type; // Sets the leave type
        this.startDate = start; // Sets the start date
        this.endDate = end; // Sets the end date
        this.totalDays = totalDays; // Sets the total working days
        this.reason = reason; // Sets the employee's reason
        this.status = status; // Sets the current status
        this.appliedAt = appliedAt; // Sets the submission timestamp
    }

    // Getters
    public Long getId() { return id; } // Returns the leave application ID
    public LeaveType getLeaveType() { return leaveType; } // Returns the leave type
    public LocalDate getStartDate() { return startDate; } // Returns the start date
    public LocalDate getEndDate() { return endDate; } // Returns the end date
    public Integer getTotalDays() { return totalDays; } // Returns the number of working days taken
    public String getReason() { return reason; } // Returns the reason for leave
    public LeaveStatus getStatus() { return status; } // Returns the current approval status
    public LocalDateTime getAppliedAt() { return appliedAt; } // Returns the submission timestamp
    public LocalDateTime getReviewedAt() { return reviewedAt; } // Returns the review timestamp (null if not yet reviewed)
    public String getReviewedBy() { return reviewedBy; } // Returns who reviewed the application (null if pending)
    public String getComments() { return comments; } // Returns reviewer's comments (null if none added)

    // Setters
    public void setId(Long id) { this.id = id; } // Sets the leave application ID
    public void setLeaveType(LeaveType leaveType) { this.leaveType = leaveType; } // Sets the leave type
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; } // Sets the start date
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; } // Sets the end date
    public void setTotalDays(Integer totalDays) { this.totalDays = totalDays; } // Sets the total working days
    public void setReason(String reason) { this.reason = reason; } // Sets the reason
    public void setStatus(LeaveStatus status) { this.status = status; } // Sets the approval status
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; } // Sets the submission timestamp
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; } // Sets the review timestamp
    public void setReviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; } // Sets who reviewed the application
    public void setComments(String comments) { this.comments = comments; } // Sets the reviewer's comments

    // Manual Builder
    public static LeaveResponseDTOBuilder builder() { // Static factory to start building a LeaveResponseDTO fluently
        return new LeaveResponseDTOBuilder(); // Returns a new builder instance
    }

    public static class LeaveResponseDTOBuilder { // Inner builder class that provides a fluent API for constructing LeaveResponseDTO
        private LeaveResponseDTO response = new LeaveResponseDTO(); // Starts with a blank DTO to populate
        public LeaveResponseDTOBuilder id(Long i) { response.setId(i); return this; } // Sets ID and returns builder
        public LeaveResponseDTOBuilder leaveType(LeaveType t) { response.setLeaveType(t); return this; } // Sets leave type and returns builder
        public LeaveResponseDTOBuilder startDate(LocalDate s) { response.setStartDate(s); return this; } // Sets start date and returns builder
        public LeaveResponseDTOBuilder endDate(LocalDate e) { response.setEndDate(e); return this; } // Sets end date and returns builder
        public LeaveResponseDTOBuilder totalDays(Integer d) { response.setTotalDays(d); return this; } // Sets total days and returns builder
        public LeaveResponseDTOBuilder reason(String r) { response.setReason(r); return this; } // Sets reason and returns builder
        public LeaveResponseDTOBuilder status(LeaveStatus s) { response.setStatus(s); return this; } // Sets status and returns builder
        public LeaveResponseDTOBuilder appliedAt(LocalDateTime a) { response.setAppliedAt(a); return this; } // Sets applied timestamp and returns builder
        public LeaveResponseDTOBuilder reviewedAt(LocalDateTime ra) { response.setReviewedAt(ra); return this; } // Sets reviewed timestamp and returns builder
        public LeaveResponseDTOBuilder reviewedBy(String rb) { response.setReviewedBy(rb); return this; } // Sets reviewer name and returns builder
        public LeaveResponseDTOBuilder comments(String c) { response.setComments(c); return this; } // Sets comments and returns builder
        public LeaveResponseDTO build() { return response; } // Returns the final constructed LeaveResponseDTO
    }
}
