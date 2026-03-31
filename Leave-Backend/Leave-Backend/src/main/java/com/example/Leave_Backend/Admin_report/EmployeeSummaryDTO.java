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

    // Setters
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public void setEmail(String email) { this.email = email; }
    public void setDepartment(String department) { this.department = department; }
    public void setBalances(Map<String, Integer> balances) { this.balances = balances; }
}
