package com.example.Leave_Backend.service.impl;

import com.example.Leave_Backend.model.dto.LeaveRequestDTO;
import com.example.Leave_Backend.model.dto.LeaveResponseDTO;
import com.example.Leave_Backend.model.entity.LeaveApplication;
import com.example.Leave_Backend.model.entity.User;
import com.example.Leave_Backend.model.entity.LeaveBalance;
import com.example.Leave_Backend.enums.LeaveType;
import com.example.Leave_Backend.enums.LeaveStatus;
import com.example.Leave_Backend.repository.LeaveRepository;
import com.example.Leave_Backend.repository.LeaveBalanceRepository;
import com.example.Leave_Backend.service.LeaveService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;
    private final LeaveBalanceRepository balanceRepository;

    public LeaveServiceImpl(LeaveRepository leaveRepository, LeaveBalanceRepository balanceRepository) {
        this.leaveRepository = leaveRepository;
        this.balanceRepository = balanceRepository;
    }

    @Override
    @Transactional
    public LeaveResponseDTO applyLeave(LeaveRequestDTO requestDTO, User user) {
        if (requestDTO.getEndDate().isBefore(requestDTO.getStartDate())) {
            throw new RuntimeException("End date cannot be before start date");
        }

        int totalDays = calculateWorkingDays(requestDTO.getStartDate(), requestDTO.getEndDate());
        if (totalDays <= 0) {
            throw new RuntimeException("Leave duration must be at least 1 working day");
        }

        if (leaveRepository.existsOverlappingLeave(user, requestDTO.getStartDate(), requestDTO.getEndDate())) {
            throw new RuntimeException("You already have an overlapping leave application for the selected dates");
        }

        LeaveBalance balance = balanceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Leave balance not found for the user"));
        
        validateBalance(balance, requestDTO.getLeaveType(), totalDays);

        LeaveApplication application = LeaveApplication.builder()
                .user(user)
                .leaveType(requestDTO.getLeaveType())
                .startDate(requestDTO.getStartDate())
                .endDate(requestDTO.getEndDate())
                .totalDays(totalDays)
                .reason(requestDTO.getReason())
                .status(LeaveStatus.PENDING)
                .appliedAt(LocalDateTime.now())
                .build();

        deductBalance(balance, requestDTO.getLeaveType(), totalDays);
        balanceRepository.save(balance);

        LeaveApplication saved = leaveRepository.save(application);
        return mapToResponse(saved);
    }

    @Override
    public List<LeaveResponseDTO> getMyLeaves(User user) {
        return leaveRepository.findByUserOrderByAppliedAtDesc(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> getLeaveBalance(User user) {
        LeaveBalance balance = balanceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));
        
        Map<String, Integer> balances = new HashMap<>();
        balances.put("ANNUAL", balance.getAnnualLeave());
        balances.put("SICK", balance.getSickLeave());
        balances.put("CASUAL", balance.getCasualLeave());
        balances.put("MATERNITY", balance.getMaternityLeave());
        balances.put("PATERNITY", balance.getPaternityLeave());
        balances.put("UNPAID", balance.getUnpaidLeave());
        return balances;
    }

    @Override
    @Transactional
    public void cancelLeave(Long leaveId, User user) {
        LeaveApplication application = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave application not found"));

        if (!application.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to cancel this leave");
        }

        if (application.getStatus() != LeaveStatus.PENDING) {
            throw new RuntimeException("Only PENDING leaves can be cancelled");
        }

        LeaveBalance balance = balanceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));
        
        refundBalance(balance, application.getLeaveType(), application.getTotalDays());
        balanceRepository.save(balance);

        leaveRepository.delete(application);
    }

    private int calculateWorkingDays(LocalDate start, LocalDate end) {
        int workingDays = 0;
        LocalDate current = start;
        while (!current.isAfter(end)) {
            if (current.getDayOfWeek() != DayOfWeek.SATURDAY && current.getDayOfWeek() != DayOfWeek.SUNDAY) {
                workingDays++;
            }
            current = current.plusDays(1);
        }
        return workingDays;
    }

    private void validateBalance(LeaveBalance balance, LeaveType type, int days) {
        int currentBalance = getBalanceByType(balance, type);
        if (type != LeaveType.UNPAID && currentBalance < days) {
            throw new RuntimeException("Insufficient " + type + " leave balance. Required: " + days + ", Available: " + currentBalance);
        }
    }

    private int getBalanceByType(LeaveBalance balance, LeaveType type) {
        return switch (type) {
            case ANNUAL -> balance.getAnnualLeave();
            case SICK -> balance.getSickLeave();
            case CASUAL -> balance.getCasualLeave();
            case MATERNITY -> balance.getMaternityLeave();
            case PATERNITY -> balance.getPaternityLeave();
            case UNPAID -> 999;
        };
    }

    private void deductBalance(LeaveBalance balance, LeaveType type, int days) {
        switch (type) {
            case ANNUAL -> balance.setAnnualLeave(balance.getAnnualLeave() - days);
            case SICK -> balance.setSickLeave(balance.getSickLeave() - days);
            case CASUAL -> balance.setCasualLeave(balance.getCasualLeave() - days);
            case MATERNITY -> balance.setMaternityLeave(balance.getMaternityLeave() - days);
            case PATERNITY -> balance.setPaternityLeave(balance.getPaternityLeave() - days);
            case UNPAID -> balance.setUnpaidLeave(balance.getUnpaidLeave() + days);
        }
    }

    private void refundBalance(LeaveBalance balance, LeaveType type, int days) {
        switch (type) {
            case ANNUAL -> balance.setAnnualLeave(balance.getAnnualLeave() + days);
            case SICK -> balance.setSickLeave(balance.getSickLeave() + days);
            case CASUAL -> balance.setCasualLeave(balance.getCasualLeave() + days);
            case MATERNITY -> balance.setMaternityLeave(balance.getMaternityLeave() + days);
            case PATERNITY -> balance.setPaternityLeave(balance.getPaternityLeave() + days);
            case UNPAID -> balance.setUnpaidLeave(balance.getUnpaidLeave() - days);
        }
    }

    private LeaveResponseDTO mapToResponse(LeaveApplication app) {
        return LeaveResponseDTO.builder()
                .id(app.getId())
                .leaveType(app.getLeaveType())
                .startDate(app.getStartDate())
                .endDate(app.getEndDate())
                .totalDays(app.getTotalDays())
                .reason(app.getReason())
                .status(app.getStatus())
                .appliedAt(app.getAppliedAt())
                .reviewedAt(app.getReviewedAt())
                .reviewedBy(app.getReviewedBy())
                .comments(app.getComments())
                .build();
    }
}
