package com.example.Leave_Backend.Admin_report;

import com.example.Leave_Backend.model.LeaveApplication;
import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Admin and Manager leave management.
 */
public interface AdminService {
    
    /**
     * Get all pending leave applications.
     */
    List<LeaveApplication> getPendingLeaves();
    
    /**
     * Approve a leave application and deduct balance.
     */
    void approveLeave(Long id);
    
    /**
     * Reject a leave application with a mandatory comment.
     */
    void rejectLeave(Long id, LeaveApprovalDTO leaveApprovalDTO);
    
    /**
     * Get all employees and their current leave balances.
     */
    List<EmployeeSummaryDTO> getEmployeeSummaries();
    
    /**
     * View all leave applications with optional filters.
     */
    List<LeaveApplication> getAllLeaves(String status, String department, LocalDate from, LocalDate to);
    
    /**
     * Get a summary report dynamically.
     */
    LeaveReportDTO getReportsSummary();
}
