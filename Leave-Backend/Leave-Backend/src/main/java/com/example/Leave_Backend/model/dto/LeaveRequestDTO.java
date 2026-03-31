package com.example.Leave_Backend.model.dto; // Declares this class belongs to the model DTO package

import com.example.Leave_Backend.enums.LeaveType; // Imports the LeaveType enum for the leave category field
import jakarta.validation.constraints.FutureOrPresent; // Validation annotation ensuring the date is today or in the future
import jakarta.validation.constraints.NotBlank; // Validation annotation ensuring a string field is not null or blank
import jakarta.validation.constraints.NotNull; // Validation annotation ensuring a field is not null
import java.time.LocalDate; // Imports LocalDate to represent start and end dates of the leave

/**
 * DTO for leave request.
 */
public class LeaveRequestDTO { // Data Transfer Object used to receive leave application data from the client in the request body

    @NotNull(message = "Leave type is required") // Validation: rejects the request if leaveType is null
    private LeaveType leaveType; // The type of leave the employee is applying for (e.g., ANNUAL, SICK, CASUAL)

    @NotNull(message = "Start date is required") // Validation: rejects the request if startDate is null
    @FutureOrPresent(message = "Start date must be in the present or future") // Validation: rejects past start dates
    private LocalDate startDate; // The date on which the employee's leave should start

    @NotNull(message = "End date is required") // Validation: rejects the request if endDate is null
    @FutureOrPresent(message = "End date must be in the present or future") // Validation: ensures end date is not in the past
    private LocalDate endDate; // The date on which the employee's leave should end

    @NotBlank(message = "Reason is required") // Validation: rejects the request if reason is blank or null
    private String reason; // The reason the employee is requesting this leave

    public LeaveRequestDTO() {} // Default constructor required for Jackson JSON deserialization

    public LeaveRequestDTO(LeaveType leaveType, LocalDate startDate, LocalDate endDate, String reason) { // Parameterized constructor for manual object creation
        this.leaveType = leaveType; // Sets the leave type
        this.startDate = startDate; // Sets the leave start date
        this.endDate = endDate; // Sets the leave end date
        this.reason = reason; // Sets the reason for leave
    }

    // Getters
    public LeaveType getLeaveType() { return leaveType; } // Returns the requested leave type
    public LocalDate getStartDate() { return startDate; } // Returns the requested start date
    public LocalDate getEndDate() { return endDate; } // Returns the requested end date
    public String getReason() { return reason; } // Returns the reason for leave

    // Setters
    public void setLeaveType(LeaveType leaveType) { this.leaveType = leaveType; } // Sets the leave type (used by Jackson during JSON deserialization)
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; } // Sets the start date
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; } // Sets the end date
    public void setReason(String reason) { this.reason = reason; } // Sets the leave reason
}
