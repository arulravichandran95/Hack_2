package com.example.Leave_Backend.enums; // Declares this file belongs to the enums package

public enum LeaveStatus { // Defines an enum type representing the possible statuses of a leave application
    PENDING,   // Leave has been submitted by the employee but not yet reviewed by admin
    APPROVED,  // Leave has been reviewed and approved by admin/manager
    REJECTED   // Leave has been reviewed and rejected by admin/manager
}
