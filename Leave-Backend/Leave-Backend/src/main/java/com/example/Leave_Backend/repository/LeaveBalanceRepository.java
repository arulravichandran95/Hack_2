package com.example.Leave_Backend.repository; // Declares this interface belongs to the repository package

import com.example.Leave_Backend.model.entity.LeaveBalance; // Imports the LeaveBalance entity this repository manages
import com.example.Leave_Backend.model.entity.User; // Imports the User entity used as a query parameter
import org.springframework.data.jpa.repository.JpaRepository; // Imports the base JPA repository providing CRUD operations
import org.springframework.stereotype.Repository; // Marks this interface as a Spring repository component

import java.util.Optional; // Imports Optional to safely handle cases where no record is found

/**
 * Repository interface for LeaveBalance entity.
 */
@Repository // Tells Spring to treat this interface as a data-access component and handle exceptions
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> { // Extends JpaRepository to get built-in CRUD + query methods for LeaveBalance with Long primary key
    Optional<LeaveBalance> findByUser(User user); // Custom query method: finds the leave balance record for a specific user; returns Optional to handle missing records safely
}
