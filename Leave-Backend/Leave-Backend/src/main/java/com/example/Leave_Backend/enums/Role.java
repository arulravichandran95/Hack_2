package com.example.Leave_Backend.enums; // Declares this file belongs to the enums package

public enum Role { // Defines the user roles supported in the leave management system
    EMPLOYEE, // Regular staff who can apply for leaves and view their own history
    MANAGER,  // Team lead/manager who can approve or reject leave requests like an admin
    ADMIN     // System administrator with full access to all leave management features
}
