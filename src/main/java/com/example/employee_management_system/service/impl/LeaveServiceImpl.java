package com.example.employee_management_system.service.impl;

import com.example.employee_management_system.entity.LeaveRequest;
import com.example.employee_management_system.repository.LeaveRepository;
import com.example.employee_management_system.service.LeaveService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository repo;

    public LeaveServiceImpl(LeaveRepository repo) {
        this.repo = repo;
    }

    @Override
    public LeaveRequest apply(LeaveRequest lr) {
        lr.setStatus("PENDING");
        lr.setAppliedAt(LocalDateTime.now());
        return repo.save(lr);
    }

    @Override
    public List<LeaveRequest> findAll() {
        return repo.findAll();
    }

    @Override
    public List<LeaveRequest> findByStatus(String status) {
        return repo.findByStatus(status);
    }

    @Override
    public List<LeaveRequest> findByEmployee(Long employeeId) {
        return repo.findByEmployeeId(employeeId);
    }

    @Override
    public LeaveRequest decide(Long id, Long approverId, boolean approve) {
        LeaveRequest lr = repo.findById(id).orElseThrow(() -> new RuntimeException("LeaveRequest not found: " + id));
        if (!"PENDING".equals(lr.getStatus())) {
            return lr; // already decided
        }
        lr.setStatus(approve ? "APPROVED" : "REJECTED");
        lr.setDecidedBy(approverId);
        lr.setDecidedAt(LocalDateTime.now());
        return repo.save(lr);
    }

    @Override
    public LeaveRequest findById(Long id) {
        return repo.findById(id).orElse(null);
    }
}
