package com.example.employee_management_system.repository;

import com.example.employee_management_system.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeaveRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByStatus(String status);
    List<LeaveRequest> findByEmployeeId(Long employeeId);
}
