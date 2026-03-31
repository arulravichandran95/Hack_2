package com.example.Leave_Backend.Admin_report;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for general employee management from admin.
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public class UserController {

    private final AdminService adminService;

    public UserController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * List all employees with their current leave balances.
     */
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeSummaryDTO>> getEmployeesWithBalances() {
        return ResponseEntity.ok(adminService.getEmployeeSummaries());
    }
}
