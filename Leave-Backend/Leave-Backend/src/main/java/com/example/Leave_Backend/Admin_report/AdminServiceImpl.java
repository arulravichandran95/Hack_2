package com.example.Leave_Backend.Admin_report;

import com.example.Leave_Backend.model.entity.LeaveApplication;
import com.example.Leave_Backend.model.entity.LeaveBalance;
import com.example.Leave_Backend.model.entity.User;
import com.example.Leave_Backend.enums.LeaveStatus;
import com.example.Leave_Backend.enums.LeaveType;
import com.example.Leave_Backend.repository.LeaveRepository;
import com.example.Leave_Backend.repository.LeaveBalanceRepository;
import com.example.Leave_Backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of AdminService.
 */
@Service
public class AdminServiceImpl implements AdminService {

    private final LeaveRepository leaveRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final UserRepository userRepository;

    public AdminServiceImpl(LeaveRepository leaveRepository, LeaveBalanceRepository leaveBalanceRepository, UserRepository userRepository) {
        this.leaveRepository = leaveRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<LeaveApplication> getPendingLeaves() {
        return leaveRepository.findByStatus(LeaveStatus.PENDING);
    }

    @Override
    @Transactional
    public void approveLeave(Long id) {
        LeaveApplication leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave application not found"));

        if (!leave.getStatus().equals(LeaveStatus.PENDING)) {
            throw new RuntimeException("Only PENDING leave applications can be approved");
        }

        User user = leave.getUser();
        LeaveType type = leave.getLeaveType();
        int daysRequested = leave.getTotalDays();

        LeaveBalance balance = leaveBalanceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("No leave balance found for user"));

        // Deduct balance based on type
        switch (type) {
            case ANNUAL -> {
                if (balance.getAnnualLeave() < daysRequested) throw new RuntimeException("Insufficient Annual Leave");
                balance.setAnnualLeave(balance.getAnnualLeave() - daysRequested);
            }
            case SICK -> {
                if (balance.getSickLeave() < daysRequested) throw new RuntimeException("Insufficient Sick Leave");
                balance.setSickLeave(balance.getSickLeave() - daysRequested);
            }
            case CASUAL -> {
                if (balance.getCasualLeave() < daysRequested) throw new RuntimeException("Insufficient Casual Leave");
                balance.setCasualLeave(balance.getCasualLeave() - daysRequested);
            }
            case MATERNITY -> {
                if (balance.getMaternityLeave() < daysRequested) throw new RuntimeException("Insufficient Maternity Leave");
                balance.setMaternityLeave(balance.getMaternityLeave() - daysRequested);
            }
            case PATERNITY -> {
                if (balance.getPaternityLeave() < daysRequested) throw new RuntimeException("Insufficient Paternity Leave");
                balance.setPaternityLeave(balance.getPaternityLeave() - daysRequested);
            }
            case UNPAID -> {
                balance.setUnpaidLeave(balance.getUnpaidLeave() + daysRequested);
            }
        }

        leaveBalanceRepository.save(balance);

        // Update leave status
        leave.setStatus(LeaveStatus.APPROVED);
        leaveRepository.save(leave);
    }

    @Override
    @Transactional
    public void rejectLeave(Long id, LeaveApprovalDTO leaveApprovalDTO) {
        if (leaveApprovalDTO.getComment() == null || leaveApprovalDTO.getComment().trim().isEmpty()) {
            throw new IllegalArgumentException("REJECTION COMMENT IS MANDATORY");
        }

        LeaveApplication leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave application not found"));

        if (!leave.getStatus().equals(LeaveStatus.PENDING)) {
            throw new RuntimeException("Only PENDING leave applications can be rejected");
        }

        leave.setStatus(LeaveStatus.REJECTED);
        leave.setComments(leaveApprovalDTO.getComment());
        leaveRepository.save(leave);
    }

    @Override
    public List<EmployeeSummaryDTO> getEmployeeSummaries() {
        List<User> employees = userRepository.findAll();
        return employees.stream().map(employee -> {
            LeaveBalance balance = leaveBalanceRepository.findByUser(employee).orElse(new LeaveBalance());
            
            Map<String, Integer> balanceMap = new HashMap<>();
            balanceMap.put("ANNUAL", balance.getAnnualLeave());
            balanceMap.put("SICK", balance.getSickLeave());
            balanceMap.put("CASUAL", balance.getCasualLeave());
            balanceMap.put("MATERNITY", balance.getMaternityLeave());
            balanceMap.put("PATERNITY", balance.getPaternityLeave());
            balanceMap.put("UNPAID", balance.getUnpaidLeave());
            
            return new EmployeeSummaryDTO(
                    employee.getId(),
                    employee.getFirstName() + " " + employee.getLastName(),
                    employee.getEmail(),
                    employee.getDepartment(),
                    balanceMap
            );
        }).collect(Collectors.toList());
    }

    @Override
    public List<LeaveApplication> getAllLeaves(String status, String department, LocalDate from, LocalDate to) {
        return leaveRepository.findAll().stream()
                .filter(l -> status == null || l.getStatus().name().equalsIgnoreCase(status))
                .filter(l -> department == null || (l.getUser().getDepartment() != null && l.getUser().getDepartment().equalsIgnoreCase(department)))
                .filter(l -> from == null || !l.getStartDate().isBefore(from))
                .filter(l -> to == null || !l.getEndDate().isAfter(to))
                .collect(Collectors.toList());
    }

    @Override
    public LeaveReportDTO getReportsSummary() {
        List<LeaveApplication> allLeaves = leaveRepository.findAll();
        
        // Use explicit map function to avoid generic Object inference issues
        Map<String, Long> typeCounts = allLeaves.stream()
                .collect(Collectors.groupingBy(l -> l.getLeaveType().name(), Collectors.counting()));
        
        Map<String, Long> statusCounts = allLeaves.stream()
                .collect(Collectors.groupingBy(l -> l.getStatus().name(), Collectors.counting()));
        
        Map<String, Long> deptCounts = allLeaves.stream()
                .filter(l -> l.getUser().getDepartment() != null)
                .collect(Collectors.groupingBy(l -> l.getUser().getDepartment(), Collectors.counting()));

        return new LeaveReportDTO(
                typeCounts,
                statusCounts,
                deptCounts
        );
    }
}
