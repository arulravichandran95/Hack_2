package com.example.Leave_Backend.model.dto;

import com.example.Leave_Backend.enums.LeaveType;
import com.example.Leave_Backend.enums.LeaveStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for leave response.
 */
public class LeaveResponseDTO {
    private Long id;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalDays;
    private String reason;
    private LeaveStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime reviewedAt;
    private String reviewedBy;
    private String comments;

    public LeaveResponseDTO() {}

    public LeaveResponseDTO(Long id, LeaveType type, LocalDate start, LocalDate end, Integer totalDays, String reason, LeaveStatus status, LocalDateTime appliedAt) {
        this.id = id;
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
    public static LeaveResponseDTOBuilder builder() {
        return new LeaveResponseDTOBuilder();
    }

    public static class LeaveResponseDTOBuilder {
        private LeaveResponseDTO response = new LeaveResponseDTO();
        public LeaveResponseDTOBuilder id(Long i) { response.setId(i); return this; }
        public LeaveResponseDTOBuilder leaveType(LeaveType t) { response.setLeaveType(t); return this; }
        public LeaveResponseDTOBuilder startDate(LocalDate s) { response.setStartDate(s); return this; }
        public LeaveResponseDTOBuilder endDate(LocalDate e) { response.setEndDate(e); return this; }
        public LeaveResponseDTOBuilder totalDays(Integer d) { response.setTotalDays(d); return this; }
        public LeaveResponseDTOBuilder reason(String r) { response.setReason(r); return this; }
        public LeaveResponseDTOBuilder status(LeaveStatus s) { response.setStatus(s); return this; }
        public LeaveResponseDTOBuilder appliedAt(LocalDateTime a) { response.setAppliedAt(a); return this; }
        public LeaveResponseDTOBuilder reviewedAt(LocalDateTime ra) { response.setReviewedAt(ra); return this; }
        public LeaveResponseDTOBuilder reviewedBy(String rb) { response.setReviewedBy(rb); return this; }
        public LeaveResponseDTOBuilder comments(String c) { response.setComments(c); return this; }
        public LeaveResponseDTO build() { return response; }
    }
}
