package com.example.employee_management_system.service;

import com.example.employee_management_system.entity.Attendance;
import com.example.employee_management_system.entity.Employee;
import com.example.employee_management_system.repository.AttendanceRepository;
import com.example.employee_management_system.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepo;
    private final EmployeeRepository employeeRepo;

    public AttendanceService(AttendanceRepository attendanceRepo, EmployeeRepository employeeRepo) {
        this.attendanceRepo = attendanceRepo;
        this.employeeRepo = employeeRepo;
    }

    // --- Existing methods ---
    public Attendance checkIn(Long empId) {
        LocalDate today = LocalDate.now();
        Employee employee = employeeRepo.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id " + empId));
        Attendance att = attendanceRepo.findByEmployeeAndDate(employee, today).orElse(new Attendance());
        att.setEmployee(employee);
        att.setDate(today);
        att.setCheckIn(LocalTime.now());
        att.setStatus("PRESENT");
        return attendanceRepo.save(att);
    }

    public Attendance checkOut(Long empId) {
        LocalDate today = LocalDate.now();
        Employee employee = employeeRepo.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id " + empId));
        Attendance att = attendanceRepo.findByEmployeeAndDate(employee, today).orElse(new Attendance());
        att.setEmployee(employee);
        att.setDate(today);
        att.setCheckOut(LocalTime.now());
        if (att.getStatus() == null) att.setStatus("PRESENT");
        return attendanceRepo.save(att);
    }

    public List<Attendance> getByEmployeeAndRange(Long empId, LocalDate start, LocalDate end) {
        Employee employee = employeeRepo.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id " + empId));
        return attendanceRepo.findByEmployeeAndDateBetween(employee, start, end);
    }

    public List<Attendance> getAllAttendance() {
        return attendanceRepo.findAll();
    }

    public List<Attendance> getAttendanceWithShift(Long empId, LocalDate start, LocalDate end) {
        Employee employee = employeeRepo.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id " + empId));

        List<Attendance> records = attendanceRepo.findByEmployeeAndDateBetween(employee, start, end);

        for (Attendance att : records) {
            if (att.getCheckIn() != null && att.getCheckOut() != null) {
                // calculate shift duration in hours
                Duration shift = Duration.between(att.getCheckIn(), att.getCheckOut());
                att.setShiftHours(shift.toMinutes() / 60.0); // assuming you add shiftHours field in Attendance
                if (att.getShiftHours() > 1) { // overtime threshold
                    att.setOvertimeHours(att.getShiftHours() - 1);
                } else {
                    att.setOvertimeHours(0.0);
                }
            } else {
                att.setShiftHours(0.0);
                att.setOvertimeHours(0.0);
            }
        }

        return records;
    }

    // --- New helper: calculate overtime hours (>1hr shift) ---
    public double calculateOvertimeHours(Long empId, LocalDate start, LocalDate end) {
        List<Attendance> records = getByEmployeeAndRange(empId, start, end);
        return records.stream()
                .mapToDouble(a -> {
                    if (a.getCheckIn() != null && a.getCheckOut() != null) {
                        long hours = Duration.between(a.getCheckIn(), a.getCheckOut()).toHours();
                        return hours > 1 ? hours - 1 : 0;
                    }
                    return 0;
                })
                .sum();
    }
}
