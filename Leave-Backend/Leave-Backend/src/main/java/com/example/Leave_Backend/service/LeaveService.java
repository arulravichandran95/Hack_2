package com.example.Leave_Backend.service; // Declares this class belongs to the service package

import com.example.Leave_Backend.model.dto.LeaveRequestDTO; // Imports the DTO for receiving leave application data
import com.example.Leave_Backend.model.dto.LeaveResponseDTO; // Imports the DTO for sending leave application data back to the client
import com.example.Leave_Backend.model.entity.User; // Imports the User entity to identify which employee is making the request
import java.util.List; // Imports List for returning multiple leave applications
import java.util.Map; // Imports Map for returning leave balance as key-value pairs (type -> days)

/**
 * Service interface for Leave management.
 */
public interface LeaveService { // Defines the business operations related to employee leave management
    LeaveResponseDTO applyLeave(LeaveRequestDTO requestDTO, User user); // Submits a new leave application for the given user and returns the created leave response
    List<LeaveResponseDTO> getMyLeaves(User user); // Retrieves all leave applications submitted by the given employee
    Map<String, Integer> getLeaveBalance(User user); // Returns a map of leave type names to remaining balance days for the given user
    void cancelLeave(Long leaveId, User user); // Cancels a pending leave application by its ID, restoring the deducted balance
}
