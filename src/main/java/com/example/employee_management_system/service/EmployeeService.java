package com.example.employee_management_system.service;

import com.example.employee_management_system.entity.Employee;
import java.util.List;

public interface EmployeeService {
    List<Employee> getAll();
    Employee getById(Long id);
    Employee getByEmail(String email);
    Employee saveEmployee(Employee employee);
    void deleteById(Long id);
}
