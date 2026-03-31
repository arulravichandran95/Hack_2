package com.example.Leave_Backend.enums; // Declares this file belongs to the enums package

/**
 * LeaveType enum for the system.
 */
public enum LeaveType { // Defines an enum for all supported leave categories an employee can apply for
    ANNUAL,    // Paid leave from the employee's annual leave quota (e.g., vacation days)
    SICK,      // Leave taken due to illness or medical reasons
    CASUAL,    // Short-term unplanned leave for personal reasons
    MATERNITY, // Extended leave for female employees for childbirth/recovery
    PATERNITY, // Short leave granted to male employees after the birth of their child
    UNPAID     // Leave taken without pay when all paid quotas are exhausted
}
