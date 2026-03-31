package com.example.Leave_Backend.model.entity;

import jakarta.persistence.*;

/**
 * LeaveBalance entity representing an employee's leave balance.
 */
@Entity
@Table(name = "leave_balances")
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer annualLeave;
    private Integer sickLeave;
    private Integer casualLeave;
    private Integer maternityLeave;
    private Integer paternityLeave;
    private Integer unpaidLeave;
    private Integer year;

    public LeaveBalance() {
        // Defaults
        this.annualLeave = 12;
        this.sickLeave = 10;
        this.casualLeave = 6;
        this.maternityLeave = 90;
        this.paternityLeave = 15;
        this.unpaidLeave = 0;
    }

    public LeaveBalance(Long id, User user, Integer annualLeave, Integer sickLeave, Integer casualLeave, Integer maternityLeave, Integer paternityLeave, Integer unpaidLeave, Integer year) {
        this.id = id;
        this.user = user;
        this.annualLeave = annualLeave;
        this.sickLeave = sickLeave;
        this.casualLeave = casualLeave;
        this.maternityLeave = maternityLeave;
        this.paternityLeave = paternityLeave;
        this.unpaidLeave = unpaidLeave;
        this.year = year;
    }

    // Getters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public Integer getAnnualLeave() { return annualLeave != null ? annualLeave : 0; }
    public Integer getSickLeave() { return sickLeave != null ? sickLeave : 0; }
    public Integer getCasualLeave() { return casualLeave != null ? casualLeave : 0; }
    public Integer getMaternityLeave() { return maternityLeave != null ? maternityLeave : 0; }
    public Integer getPaternityLeave() { return paternityLeave != null ? paternityLeave : 0; }
    public Integer getUnpaidLeave() { return unpaidLeave != null ? unpaidLeave : 0; }
    public Integer getYear() { return year; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setAnnualLeave(Integer annualLeave) { this.annualLeave = annualLeave; }
    public void setSickLeave(Integer sickLeave) { this.sickLeave = sickLeave; }
    public void setCasualLeave(Integer casualLeave) { this.casualLeave = casualLeave; }
    public void setMaternityLeave(Integer maternityLeave) { this.maternityLeave = maternityLeave; }
    public void setPaternityLeave(Integer paternityLeave) { this.paternityLeave = paternityLeave; }
    public void setUnpaidLeave(Integer unpaidLeave) { this.unpaidLeave = unpaidLeave; }
    public void setYear(Integer year) { this.year = year; }

    // Manual Builder
    public static LeaveBalanceBuilder builder() {
        return new LeaveBalanceBuilder();
    }

    public static class LeaveBalanceBuilder {
        private LeaveBalance balance = new LeaveBalance();
        public LeaveBalanceBuilder user(User u) { balance.setUser(u); return this; }
        public LeaveBalanceBuilder annualLeave(Integer a) { balance.setAnnualLeave(a); return this; }
        public LeaveBalanceBuilder sickLeave(Integer s) { balance.setSickLeave(s); return this; }
        public LeaveBalanceBuilder casualLeave(Integer c) { balance.setCasualLeave(c); return this; }
        public LeaveBalanceBuilder year(Integer y) { balance.setYear(y); return this; }
        public LeaveBalance build() { return balance; }
    }
}
