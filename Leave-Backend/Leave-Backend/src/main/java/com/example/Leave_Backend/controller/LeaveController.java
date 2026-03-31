package com.example.Leave_Backend.controller; // Declares this class belongs to the controller package

import com.example.Leave_Backend.model.dto.LeaveRequestDTO; // Imports the DTO for receiving leave application data
import com.example.Leave_Backend.model.dto.LeaveResponseDTO; // Imports the DTO for sending leave application details back to the client
import com.example.Leave_Backend.model.entity.User; // Imports the User entity to identify the currently authenticated employee
import com.example.Leave_Backend.service.LeaveService; // Imports the service containing the leave management business logic
import jakarta.validation.Valid; // Enables Bean Validation on request body fields
import org.springframework.http.ResponseEntity; // Wraps the response with both body and HTTP status code
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Injects the currently authenticated user from the security context
import org.springframework.web.bind.annotation.*; // Imports all REST mapping annotations (GET, POST, DELETE, etc.)

import java.util.List; // Imports List for returning multiple leave records
import java.util.Map; // Imports Map for returning leave balance as type-to-days key-value pairs

/**
 * Controller for employee leave applications.
 */
@RestController // Marks this as a REST API controller; all responses are auto-serialized to JSON
@RequestMapping("/api/leaves") // Base URL for all leave-related endpoints in this controller
@CrossOrigin(origins = "*") // Allows cross-origin requests from any origin (for frontend access during development)
public class LeaveController { // Controller that handles employee leave application operations

    private final LeaveService leaveService; // Service containing all business logic for leave management

    public LeaveController(LeaveService leaveService) { // Constructor injection of LeaveService (preferred over @Autowired on fields)
        this.leaveService = leaveService; // Stores the injected service reference
    }

    @PostMapping("/apply") // Maps HTTP POST /api/leaves/apply to this method
    public ResponseEntity<LeaveResponseDTO> applyLeave(
            @Valid @RequestBody LeaveRequestDTO requestDTO, // @Valid runs validation on the incoming JSON; @RequestBody deserializes JSON to DTO
            @AuthenticationPrincipal User user) { // Injects the currently logged-in User from the JWT authentication context
        return ResponseEntity.ok(leaveService.applyLeave(requestDTO, user)); // Calls service to apply leave and returns the result with HTTP 200
    }

    @GetMapping("/my-leaves") // Maps HTTP GET /api/leaves/my-leaves to this method
    public ResponseEntity<List<LeaveResponseDTO>> getMyLeaves(
            @AuthenticationPrincipal User user) { // Injects the currently authenticated user to fetch only their leaves
        return ResponseEntity.ok(leaveService.getMyLeaves(user)); // Returns all leave applications for the logged-in user
    }

    @GetMapping("/balance") // Maps HTTP GET /api/leaves/balance to this method
    public ResponseEntity<Map<String, Integer>> getLeaveBalance(
            @AuthenticationPrincipal User user) { // Injects the current user to look up their specific balance
        return ResponseEntity.ok(leaveService.getLeaveBalance(user)); // Returns a map of leave type -> remaining days as JSON
    }

    @DeleteMapping("/{id}") // Maps HTTP DELETE /api/leaves/{id} to this method
    public ResponseEntity<String> cancelLeave(
            @PathVariable Long id, // Extracts the leave application ID from the URL path
            @AuthenticationPrincipal User user) { // Injects the current user to verify ownership of the leave
        leaveService.cancelLeave(id, user); // Calls service to cancel the leave and restore the balance
        return ResponseEntity.ok("Leave application cancelled successfully"); // Returns a success message with HTTP 200
    }
}
