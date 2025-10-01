package com.example.employee_management_system.service;

import com.example.employee_management_system.entity.Department;
import com.example.employee_management_system.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getById(Long id) {
        Optional<Department> dept = departmentRepository.findById(id);
        return dept.orElse(null); // return null if not found
    }
}
