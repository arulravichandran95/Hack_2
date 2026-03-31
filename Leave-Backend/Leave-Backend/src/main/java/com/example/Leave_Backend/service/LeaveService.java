package com.example.Leave_Backend.service;

import com.example.Leave_Backend.model.dto.LeaveRequestDTO;
import com.example.Leave_Backend.model.dto.LeaveResponseDTO;
import com.example.Leave_Backend.model.entity.User;
import java.util.List;
import java.util.Map;

public interface LeaveService {
    LeaveResponseDTO applyLeave(LeaveRequestDTO requestDTO, User user);
    List<LeaveResponseDTO> getMyLeaves(User user);
    Map<String, Double> getLeaveBalance(User user);
    void cancelLeave(Long leaveId, User user);
}
