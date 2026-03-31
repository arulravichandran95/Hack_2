package com.example.Leave_Backend.model.entity;

import com.example.Leave_Backend.enums.LeaveType;
import com.example.Leave_Backend.enums.LeaveStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * LeaveApplication entity representing an application in the system.
 */
@Entity
@Table(name = "leave_applications")
public class LeaveApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveType leaveType;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Integer totalDays;

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus status;

    @Column(nullable = false)
    private LocalDateTime appliedAt;

    private LocalDateTime reviewedAt;

    private String reviewedBy;

    private String comments;

    public LeaveApplication() {}

    public LeaveApplication(Long id, User user, LeaveType type, LocalDate start, LocalDate end, Integer totalDays, String reason, LeaveStatus status, LocalDateTime appliedAt) {
        this.id = id;
        this.user = user;
        this.leaveType = type;
        this.startDate = start;
        this.endDate = end;
        this.totalDays = totalDays;
        this.reason = reason;
        this.status = status;
        this.appliedAt = appliedAt;
    }

    // Getters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public LeaveType getLeaveType() { return leaveType; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public Integer getTotalDays() { return totalDays; }
    public String getReason() { return reason; }
    public LeaveStatus getStatus() { return status; }
    public LocalDateTime getAppliedAt() { return appliedAt; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public String getReviewedBy() { return reviewedBy; }
    public String getComments() { return comments; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setLeaveType(LeaveType leaveType) { this.leaveType = leaveType; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setTotalDays(Integer totalDays) { this.totalDays = totalDays; }
    public void setReason(String reason) { this.reason = reason; }
    public void setStatus(LeaveStatus status) { this.status = status; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
    public void setReviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; }
    public void setComments(String comments) { this.comments = comments; }

    // Manual Builder
    public static LeaveApplicationBuilder builder() {
        return new LeaveApplicationBuilder();
    }

    public static class LeaveApplicationBuilder {
        private LeaveApplication app = new LeaveApplication();
        public LeaveApplicationBuilder user(User u) { app.setUser(u); return this; }
        public LeaveApplicationBuilder leaveType(LeaveType t) { app.setLeaveType(t); return this; }
        public LeaveApplicationBuilder startDate(LocalDate s) { app.setStartDate(s); return this; }
        public LeaveApplicationBuilder endDate(LocalDate e) { app.setEndDate(e); return this; }
        public LeaveApplicationBuilder totalDays(Integer d) { app.setTotalDays(d); return this; }
        public LeaveApplicationBuilder reason(String r) { app.setReason(r); return this; }
        public LeaveApplicationBuilder status(LeaveStatus s) { app.setStatus(s); return this; }
        public LeaveApplicationBuilder appliedAt(LocalDateTime a) { app.setAppliedAt(a); return this; }
        public LeaveApplication build() { return app; }
    }
}
