package com.example.Leave_Backend.repository; // Declares this interface belongs to the repository package

import com.example.Leave_Backend.model.entity.LeaveApplication; // Imports the LeaveApplication entity this repository manages
import com.example.Leave_Backend.model.entity.User; // Imports User entity used as a query parameter
import com.example.Leave_Backend.enums.LeaveStatus; // Imports the LeaveStatus enum for filtering by status
import org.springframework.data.jpa.repository.JpaRepository; // Imports base JPA repository with built-in CRUD operations
import org.springframework.data.jpa.repository.Query; // Imports annotation to define custom JPQL queries
import org.springframework.data.repository.query.Param; // Imports Param annotation to bind named parameters in custom queries
import org.springframework.stereotype.Repository; // Marks this as a Spring Data repository component

import java.time.LocalDate; // Imports LocalDate used as date parameters in the overlap check query
import java.util.List; // Imports List as the return type for queries that return multiple results

/**
 * Repository interface for LeaveApplication entity.
 */
@Repository // Registers this interface as a Spring-managed repository bean
public interface LeaveRepository extends JpaRepository<LeaveApplication, Long> { // Extends JpaRepository for built-in CRUD + pagination on LeaveApplication with Long ID

    List<LeaveApplication> findByStatus(LeaveStatus status); // Spring Data auto-generates a query to fetch all leave applications with a given status (e.g., all PENDING leaves)

    List<LeaveApplication> findByUserOrderByAppliedAtDesc(User user); // Auto-generated query to fetch all leaves for a specific user, ordered from newest to oldest

    @Query("SELECT COUNT(l) > 0 FROM LeaveApplication l WHERE l.user = :user " + // Custom JPQL query counting existing non-rejected leaves for the user in a date range
           "AND l.status <> 'REJECTED' " + // Excludes REJECTED leaves since they don't block new applications
           "AND ((l.startDate <= :endDate AND l.endDate >= :startDate))") // Checks for any date overlap between existing and new leave
    boolean existsOverlappingLeave(@Param("user") User user, // Named parameter binding for the user
                                   @Param("startDate") LocalDate startDate, // Named parameter for the requested start date
                                   @Param("endDate") LocalDate endDate); // Named parameter for the requested end date; returns true if overlap exists
}
