package com.example.Leave_Backend.Admin_report;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for approving or rejecting leave requests.
 */
public class LeaveApprovalDTO {
    
    @NotBlank(message = "Comment is mandatory for rejection")
    private String comment;

    public LeaveApprovalDTO() {}

    public LeaveApprovalDTO(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
