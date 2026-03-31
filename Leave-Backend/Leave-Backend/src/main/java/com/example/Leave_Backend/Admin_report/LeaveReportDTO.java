package com.example.Leave_Backend.Admin_report;

import java.util.Map;

/**
 * DTO for summarizes leave statistics.
 */
public class LeaveReportDTO {
    private Map<String, Long> totalLeavesByType; // LeaveType -> Count
    private Map<String, Long> statusCounts; // Status -> Count
    private Map<String, Long> departmentWiseBreakdown; // Department -> Count

    public LeaveReportDTO() {}

    public LeaveReportDTO(Map<String, Long> totalLeavesByType, Map<String, Long> statusCounts, Map<String, Long> departmentWiseBreakdown) {
        this.totalLeavesByType = totalLeavesByType;
        this.statusCounts = statusCounts;
        this.departmentWiseBreakdown = departmentWiseBreakdown;
    }

    // Getters
    public Map<String, Long> getTotalLeavesByType() { return totalLeavesByType; }
    public Map<String, Long> getStatusCounts() { return statusCounts; }
    public Map<String, Long> getDepartmentWiseBreakdown() { return departmentWiseBreakdown; }

    // Builder
    public static LeaveReportDTOBuilder builder() {
        return new LeaveReportDTOBuilder();
    }

    public static class LeaveReportDTOBuilder {
        private Map<String, Long> totalLeavesByType;
        private Map<String, Long> statusCounts;
        private Map<String, Long> departmentWiseBreakdown;

        public LeaveReportDTOBuilder totalLeavesByType(Map<String, Long> totalLeavesByType) { this.totalLeavesByType = totalLeavesByType; return this; }
        public LeaveReportDTOBuilder statusCounts(Map<String, Long> statusCounts) { this.statusCounts = statusCounts; return this; }
        public LeaveReportDTOBuilder departmentWiseBreakdown(Map<String, Long> departmentWiseBreakdown) { this.departmentWiseBreakdown = departmentWiseBreakdown; return this; }

        public LeaveReportDTO build() {
            return new LeaveReportDTO(totalLeavesByType, statusCounts, departmentWiseBreakdown);
        }
    }
}
