package com.example.employee_management_system.service;

import com.example.employee_management_system.entity.LeaveRequest;
import java.util.List;

public interface LeaveService {
    LeaveRequest apply(LeaveRequest lr);
    List<LeaveRequest> findAll();
    List<LeaveRequest> findByStatus(String status);
    List<LeaveRequest> findByEmployee(Long employeeId);
    LeaveRequest decide(Long id, Long approverId, boolean approve);
    LeaveRequest findById(Long id);
}
