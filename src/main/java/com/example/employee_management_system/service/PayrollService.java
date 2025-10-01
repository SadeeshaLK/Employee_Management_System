package com.example.employee_management_system.service;

import com.example.employee_management_system.entity.Payroll;
import java.util.List;

public interface PayrollService {

    List<Payroll> findAll();
    Payroll findById(Long id);
    Payroll save(Payroll payroll);
    void delete(Long id);

    // Optional: PDF generation
    byte[] generatePdf(Payroll payroll);
}
