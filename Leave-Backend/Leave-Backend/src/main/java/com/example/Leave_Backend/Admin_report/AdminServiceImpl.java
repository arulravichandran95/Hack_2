package com.example.Leave_Backend.Admin_report;

import com.example.Leave_Backend.model.LeaveApplication;
import com.example.Leave_Backend.model.LeaveBalance;
import com.example.Leave_Backend.model.User;
import com.example.Leave_Backend.model.LeaveStatus;
import com.example.Leave_Backend.model.LeaveType;
import com.example.Leave_Backend.repository.LeaveRepository;
import com.example.Leave_Backend.repository.LeaveBalanceRepository;
import com.example.Leave_Backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

        User employee = leave.getEmployee();
        LeaveType type = leave.getLeaveType();
        long daysRequested = ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;

        LeaveBalance balance = leaveBalanceRepository.findByEmployeeAndLeaveType(employee, type)
                .orElseThrow(() -> new RuntimeException("No leave balance found for employee"));

        if (balance.getRemainingDays() < daysRequested) {
            throw new RuntimeException("Insufficient leave balance for the requested leave type");
        }

        // Deduct balance
        balance.setRemainingDays(balance.getRemainingDays() - (int) daysRequested);
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
        leave.setAdminComment(leaveApprovalDTO.getComment());
        leaveRepository.save(leave);
    }

    @Override
    public List<EmployeeSummaryDTO> getEmployeeSummaries() {
        List<User> employees = userRepository.findAll();
        return employees.stream().map(employee -> {
            List<LeaveBalance> balances = leaveBalanceRepository.findByEmployee(employee);
            Map<String, Integer> balanceMap = balances.stream()
                    .collect(Collectors.toMap(b -> b.getLeaveType().name(), LeaveBalance::getRemainingDays));
            
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
        // Simple dynamic query using stream filtering for demo (Ideally use Specification API)
        return leaveRepository.findAll().stream()
                .filter(l -> status == null || l.getStatus().name().equalsIgnoreCase(status))
                .filter(l -> department == null || (l.getEmployee().getDepartment() != null && l.getEmployee().getDepartment().equalsIgnoreCase(department)))
                .filter(l -> from == null || !l.getStartDate().isBefore(from))
                .filter(l -> to == null || !l.getEndDate().isAfter(to))
                .collect(Collectors.toList());
    }

    @Override
    public LeaveReportDTO getReportsSummary() {
        List<LeaveApplication> allLeaves = leaveRepository.findAll();
        
        // Total leaves per type
        Map<String, Long> typeCounts = allLeaves.stream()
                .collect(Collectors.groupingBy(l -> l.getLeaveType().name(), Collectors.counting()));
        
        // Status counts
        Map<String, Long> statusCounts = allLeaves.stream()
                .collect(Collectors.groupingBy(l -> l.getStatus().name(), Collectors.counting()));
        
        // Department-wise breakdown
        Map<String, Long> deptCounts = allLeaves.stream()
                .collect(Collectors.groupingBy(l -> l.getEmployee().getDepartment(), Collectors.counting()));

        return new LeaveReportDTO(
                typeCounts,
                statusCounts,
                deptCounts
        );
    }
}
