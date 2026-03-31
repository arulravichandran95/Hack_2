package com.example.Leave_Backend.repository;

import com.example.Leave_Backend.model.entity.LeaveBalance;
import com.example.Leave_Backend.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    Optional<LeaveBalance> findByUser(User user);
}
