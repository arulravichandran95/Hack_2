package com.example.Leave_Backend.Admin_report;

import com.example.Leave_Backend.model.entity.LeaveApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for Admin and Manager leave management.
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * List all PENDING leave applications.
     */
    @GetMapping("/leaves/pending")
    public ResponseEntity<List<LeaveApplication>> getPendingLeaves() {
        return ResponseEntity.ok(adminService.getPendingLeaves());
    }

    /**
     * Approve a leave application.
     */
    @PutMapping("/leaves/{id}/approve")
    public ResponseEntity<String> approveLeave(@PathVariable Long id) {
        adminService.approveLeave(id);
        return ResponseEntity.ok("Leave application approved successfully");
    }

    /**
     * Reject a leave application with a mandatory comment.
     */
    @PutMapping("/leaves/{id}/reject")
    public ResponseEntity<String> rejectLeave(@PathVariable Long id, @RequestBody LeaveApprovalDTO leaveApprovalDTO) {
        adminService.rejectLeave(id, leaveApprovalDTO);
        return ResponseEntity.ok("Leave application rejected successfully");
    }

    /**
     * View all leave applications with optional filtering.
     */
    @GetMapping("/leaves/all")
    public ResponseEntity<List<LeaveApplication>> getAllLeaves(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(adminService.getAllLeaves(status, department, from, to));
    }

    /**
     * Get a summary report dynamically.
     */
    @GetMapping("/reports/summary")
    public ResponseEntity<LeaveReportDTO> getReportsSummary() {
        return ResponseEntity.ok(adminService.getReportsSummary());
    }
}
