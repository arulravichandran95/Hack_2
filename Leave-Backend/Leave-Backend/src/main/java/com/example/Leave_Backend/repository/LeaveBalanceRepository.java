package com.example.Leave_Backend.repository;

import com.example.Leave_Backend.model.LeaveBalance;
import com.example.Leave_Backend.model.LeaveType;
import com.example.Leave_Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for LeaveBalance entity.
 */
@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    List<LeaveBalance> findByEmployee(User employee);
    Optional<LeaveBalance> findByEmployeeAndLeaveType(User employee, LeaveType leaveType);
    Optional<LeaveBalance> findByEmployee(Optional<User> user);
}
