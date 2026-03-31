package com.example.Leave_Backend.repository;

import com.example.Leave_Backend.model.LeaveApplication;
import com.example.Leave_Backend.model.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for LeaveApplication entity.
 */
@Repository
public interface LeaveRepository extends JpaRepository<LeaveApplication, Long> {
    List<LeaveApplication> findByStatus(LeaveStatus status);
}
