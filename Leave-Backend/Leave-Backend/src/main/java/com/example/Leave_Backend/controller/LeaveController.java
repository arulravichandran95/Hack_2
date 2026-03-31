package com.example.Leave_Backend.controller;

import com.example.Leave_Backend.model.dto.LeaveRequestDTO;
import com.example.Leave_Backend.model.dto.LeaveResponseDTO;
import com.example.Leave_Backend.model.entity.User;
import com.example.Leave_Backend.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller for employee leave applications.
 */
@RestController
@RequestMapping("/api/leaves")
@CrossOrigin(origins = "*")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("/apply")
    public ResponseEntity<LeaveResponseDTO> applyLeave(
            @Valid @RequestBody LeaveRequestDTO requestDTO,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(leaveService.applyLeave(requestDTO, user));
    }

    @GetMapping("/my-leaves")
    public ResponseEntity<List<LeaveResponseDTO>> getMyLeaves(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(leaveService.getMyLeaves(user));
    }

    @GetMapping("/balance")
    public ResponseEntity<Map<String, Integer>> getLeaveBalance(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(leaveService.getLeaveBalance(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelLeave(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        leaveService.cancelLeave(id, user);
        return ResponseEntity.ok("Leave application cancelled successfully");
    }
}
