package com.example.Leave_Backend.repository;

import com.example.Leave_Backend.model.entity.LeaveApplication;
import com.example.Leave_Backend.model.entity.User;
import com.example.Leave_Backend.model.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveApplication, Long> {

    List<LeaveApplication> findByUserOrderByAppliedAtDesc(User user);

    @Query("SELECT COUNT(l) > 0 FROM LeaveApplication l WHERE l.user = :user " +
           "AND l.status <> 'REJECTED' " +
           "AND ((l.startDate <= :endDate AND l.endDate >= :startDate))")
    boolean existsOverlappingLeave(@Param("user") User user, 
                                   @Param("startDate") LocalDate startDate, 
                                   @Param("endDate") LocalDate endDate);
}
