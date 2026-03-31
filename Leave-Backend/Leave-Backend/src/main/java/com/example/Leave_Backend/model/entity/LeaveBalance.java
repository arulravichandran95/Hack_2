package com.example.Leave_Backend.model.entity; // Declares this class belongs to the model entity package

import jakarta.persistence.*; // Imports all JPA annotations for entity mapping

/**
 * LeaveBalance entity representing an employee's leave balance.
 */
@Entity // Marks this class as a JPA entity that maps to a database table
@Table(name = "leave_balances") // Specifies the DB table name as "leave_balances"
public class LeaveBalance { // Represents the remaining leave entitlement for each employee per year

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID using the DB identity column
    private Long id; // Unique identifier for each leave balance record

    @OneToOne(fetch = FetchType.LAZY) // Each user has exactly one leave balance; loaded lazily for performance
    @JoinColumn(name = "user_id", nullable = false) // Maps to a foreign key "user_id" column in this table; cannot be null
    private User user; // The employee this leave balance belongs to

    private Integer annualLeave; // Remaining annual (paid vacation) leave days for the employee
    private Integer sickLeave; // Remaining sick leave days for the employee
    private Integer casualLeave; // Remaining casual leave days for the employee
    private Integer maternityLeave; // Remaining maternity leave days for the employee
    private Integer paternityLeave; // Remaining paternity leave days for the employee
    private Integer unpaidLeave; // Count of unpaid leave days taken (incremented, not decremented)
    private Integer year; // The year this leave balance applies to (e.g., 2024)

    public LeaveBalance() { // Default constructor; sets initial leave balances when a new balance record is created
        // Defaults
        this.annualLeave = 12; // New employees start with 12 annual leave days per year
        this.sickLeave = 10; // New employees start with 10 sick leave days per year
        this.casualLeave = 6; // New employees start with 6 casual leave days per year
        this.maternityLeave = 90; // New employees start with 90 maternity leave days (standard policy)
        this.paternityLeave = 15; // New employees start with 15 paternity leave days
        this.unpaidLeave = 0; // Unpaid leave count starts at 0 for all new employees
    }

    public LeaveBalance(Long id, User user, Integer annualLeave, Integer sickLeave, Integer casualLeave, Integer maternityLeave, Integer paternityLeave, Integer unpaidLeave, Integer year) { // Parameterized constructor for creating a leave balance with specific values
        this.id = id; // Sets the unique record ID
        this.user = user; // Sets the associated employee
        this.annualLeave = annualLeave; // Sets the annual leave balance
        this.sickLeave = sickLeave; // Sets the sick leave balance
        this.casualLeave = casualLeave; // Sets the casual leave balance
        this.maternityLeave = maternityLeave; // Sets the maternity leave balance
        this.paternityLeave = paternityLeave; // Sets the paternity leave balance
        this.unpaidLeave = unpaidLeave; // Sets the unpaid leave count
        this.year = year; // Sets the year this balance is for
    }

    // Getters
    public Long getId() { return id; } // Returns the unique ID of this leave balance record
    public User getUser() { return user; } // Returns the employee this balance belongs to
    public Integer getAnnualLeave() { return annualLeave != null ? annualLeave : 0; } // Returns annual leave balance; returns 0 if null to avoid NullPointerException
    public Integer getSickLeave() { return sickLeave != null ? sickLeave : 0; } // Returns sick leave balance; defaults to 0 if null
    public Integer getCasualLeave() { return casualLeave != null ? casualLeave : 0; } // Returns casual leave balance; defaults to 0 if null
    public Integer getMaternityLeave() { return maternityLeave != null ? maternityLeave : 0; } // Returns maternity leave balance; defaults to 0 if null
    public Integer getPaternityLeave() { return paternityLeave != null ? paternityLeave : 0; } // Returns paternity leave balance; defaults to 0 if null
    public Integer getUnpaidLeave() { return unpaidLeave != null ? unpaidLeave : 0; } // Returns unpaid leave count; defaults to 0 if null
    public Integer getYear() { return year; } // Returns the year this leave balance is associated with

    // Setters
    public void setId(Long id) { this.id = id; } // Updates the record ID
    public void setUser(User user) { this.user = user; } // Updates the associated employee
    public void setAnnualLeave(Integer annualLeave) { this.annualLeave = annualLeave; } // Updates the annual leave balance (decremented on approval)
    public void setSickLeave(Integer sickLeave) { this.sickLeave = sickLeave; } // Updates the sick leave balance
    public void setCasualLeave(Integer casualLeave) { this.casualLeave = casualLeave; } // Updates the casual leave balance
    public void setMaternityLeave(Integer maternityLeave) { this.maternityLeave = maternityLeave; } // Updates the maternity leave balance
    public void setPaternityLeave(Integer paternityLeave) { this.paternityLeave = paternityLeave; } // Updates the paternity leave balance
    public void setUnpaidLeave(Integer unpaidLeave) { this.unpaidLeave = unpaidLeave; } // Updates the unpaid leave count
    public void setYear(Integer year) { this.year = year; } // Updates the year of this balance record

    // Manual Builder
    public static LeaveBalanceBuilder builder() { // Static factory to begin building a LeaveBalance object fluently
        return new LeaveBalanceBuilder(); // Returns a new builder instance
    }

    public static class LeaveBalanceBuilder { // Inner builder class for fluently constructing LeaveBalance objects
        private LeaveBalance balance = new LeaveBalance(); // Creates a new LeaveBalance with default values as the starting point
        public LeaveBalanceBuilder user(User u) { balance.setUser(u); return this; } // Sets the associated user and chains the builder
        public LeaveBalanceBuilder annualLeave(Integer a) { balance.setAnnualLeave(a); return this; } // Overrides the default annual leave value and chains
        public LeaveBalanceBuilder sickLeave(Integer s) { balance.setSickLeave(s); return this; } // Overrides the default sick leave value and chains
        public LeaveBalanceBuilder casualLeave(Integer c) { balance.setCasualLeave(c); return this; } // Overrides the default casual leave value and chains
        public LeaveBalanceBuilder year(Integer y) { balance.setYear(y); return this; } // Sets the year for this balance record and chains
        public LeaveBalance build() { return balance; } // Returns the final constructed LeaveBalance object
    }
}
