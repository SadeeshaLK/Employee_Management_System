package com.example.employee_management_system.repository;

import com.example.employee_management_system.entity.Attendance;
import com.example.employee_management_system.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // ✅ Now takes Employee, not empId
    Optional<Attendance> findByEmployeeAndDate(Employee employee, LocalDate date);
    List<Attendance> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Attendance> findByEmployeeAndDateBetween(Employee employee, LocalDate start, LocalDate end);
}
