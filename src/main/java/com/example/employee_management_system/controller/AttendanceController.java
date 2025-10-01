package com.example.employee_management_system.controller;

import com.example.employee_management_system.entity.Employee;
import com.example.employee_management_system.service.AttendanceService;
import com.example.employee_management_system.service.EmployeeService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final EmployeeService employeeService;

    public AttendanceController(AttendanceService attendanceService, EmployeeService employeeService) {
        this.attendanceService = attendanceService;
        this.employeeService = employeeService;
    }


    // Attendance dashboard
    @GetMapping
    public String list(Model model, Authentication auth) {
        String username = auth.getName();
        List<String> roles = auth.getAuthorities().stream().map(r -> r.getAuthority()).toList();

        // Employee can only see themselves
        if (roles.contains("ROLE_EMPLOYEE") && !roles.contains("ROLE_HR") && !roles.contains("ROLE_ADMIN")) {
            Employee emp = employeeService.getByEmail(username); // make sure getByEmail exists
            model.addAttribute("employees", List.of(emp));
        } else { // HR/Admin can see all employees
            model.addAttribute("employees", employeeService.getAll());
        }

        model.addAttribute("username", username);
        model.addAttribute("roles", roles);
        return "attendance/list";
    }

    @PostMapping("/checkin")
    public String checkIn(@RequestParam Long empId, Authentication auth) {
        String username = auth.getName();
        List<String> roles = auth.getAuthorities().stream().map(r -> r.getAuthority()).toList();

        // Employees can only check in themselves
        if (roles.contains("ROLE_EMPLOYEE") && !roles.contains("ROLE_HR") && !roles.contains("ROLE_ADMIN")) {
            Employee emp = employeeService.getByEmail(username);
            if (!emp.getId().equals(empId)) return "redirect:/attendance?error=unauthorized";
        }
        attendanceService.checkIn(empId);
        return "redirect:/attendance";
    }

    @PostMapping("/checkout")
    public String checkOut(@RequestParam Long empId, Authentication auth) {
        String username = auth.getName();
        List<String> roles = auth.getAuthorities().stream().map(r -> r.getAuthority()).toList();

        if (roles.contains("ROLE_EMPLOYEE") && !roles.contains("ROLE_HR") && !roles.contains("ROLE_ADMIN")) {
            Employee emp = employeeService.getByEmail(username);
            if (!emp.getId().equals(empId)) return "redirect:/attendance?error=unauthorized";
        }
        attendanceService.checkOut(empId);
        return "redirect:/attendance";
    }

    // HR/Admin view all attendance records
    @GetMapping("/records")
    public String viewAllRecords(Model model, Authentication auth) {
        List<String> roles = auth.getAuthorities().stream().map(r -> r.getAuthority()).toList();
        if (!roles.contains("ROLE_HR") && !roles.contains("ROLE_ADMIN")) {
            return "redirect:/attendance?error=unauthorized";
        }
        model.addAttribute("records", attendanceService.getAllAttendance());
        return "attendance/records";
    }

    // HR view employee records with shift time
    @GetMapping("/records/{empId}")
    public String viewEmployeeRecords(@PathVariable Long empId,
                                      @RequestParam(required = false) String start,
                                      @RequestParam(required = false) String end,
                                      Model model,
                                      Authentication auth) {

        List<String> roles = auth.getAuthorities().stream().map(r -> r.getAuthority()).toList();
        if (!roles.contains("ROLE_HR") && !roles.contains("ROLE_ADMIN")) {
            return "redirect:/attendance?error=unauthorized";
        }

        LocalDate startDate = start != null ? LocalDate.parse(start) : LocalDate.now().minusDays(7);
        LocalDate endDate = end != null ? LocalDate.parse(end) : LocalDate.now();

        var attendanceList = attendanceService.getAttendanceWithShift(empId, startDate, endDate);
        Employee emp = employeeService.getById(empId);

        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("employee", emp);
        return "attendance/employee-records";
    }
}
