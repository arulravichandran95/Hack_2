package com.example.Leave_Backend.Admin_report;

import java.util.Map;

/**
 * DTO for employee summary with leave balances.
 */
public class EmployeeSummaryDTO {
    private Long employeeId;
    private String employeeName;
    private String email;
    private String department;
    private Map<String, Integer> balances; // LeaveType name -> balance

    public EmployeeSummaryDTO() {}

    public EmployeeSummaryDTO(Long employeeId, String employeeName, String email, String department, Map<String, Integer> balances) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.email = email;
        this.department = department;
        this.balances = balances;
    }

    // Getters
    public Long getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public String getEmail() { return email; }
    public String getDepartment() { return department; }
    public Map<String, Integer> getBalances() { return balances; }

    // Builder
    public static EmployeeSummaryDTOBuilder builder() {
        return new EmployeeSummaryDTOBuilder();
    }

    public static class EmployeeSummaryDTOBuilder {
        private Long employeeId;
        private String employeeName;
        private String email;
        private String department;
        private Map<String, Integer> balances;

        public EmployeeSummaryDTOBuilder employeeId(Long employeeId) { this.employeeId = employeeId; return this; }
        public EmployeeSummaryDTOBuilder employeeName(String employeeName) { this.employeeName = employeeName; return this; }
        public EmployeeSummaryDTOBuilder email(String email) { this.email = email; return this; }
        public EmployeeSummaryDTOBuilder department(String department) { this.department = department; return this; }
        public EmployeeSummaryDTOBuilder balances(Map<String, Integer> balances) { this.balances = balances; return this; }

        public EmployeeSummaryDTO build() {
            return new EmployeeSummaryDTO(employeeId, employeeName, email, department, balances);
        }
    }
}
