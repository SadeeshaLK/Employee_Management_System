package com.example.employee_management_system.controller;

import com.example.employee_management_system.entity.Payroll;
import com.example.employee_management_system.entity.Employee;
import com.example.employee_management_system.entity.Attendance;
import com.example.employee_management_system.service.PayrollService;
import com.example.employee_management_system.service.EmployeeService;
import com.example.employee_management_system.service.AttendanceService;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.Duration;

@Controller
@RequestMapping("/payrolls")
public class PayrollController {

    private final PayrollService payrollService;
    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;

    public PayrollController(PayrollService payrollService, EmployeeService employeeService, AttendanceService attendanceService) {
        this.payrollService = payrollService;
        this.employeeService = employeeService;
        this.attendanceService = attendanceService;
    }

    @GetMapping({"", "/list"})
    public String list(Model model) {
        List<Payroll> payrolls = payrollService.findAll();
        model.addAttribute("payrolls", payrolls);
        return "payroll/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("payroll", new Payroll());
        model.addAttribute("employees", employeeService.getAll());
        return "payroll/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("payroll") Payroll payroll, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("employees", employeeService.getAll());
            return "payroll/form";
        }

        Employee emp = employeeService.getById(payroll.getEmployee().getId());
        payroll.setEmployee(emp);
        payrollService.save(payroll);
        return "redirect:/payrolls/list";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Payroll payroll = payrollService.findById(id);
        model.addAttribute("payroll", payroll);
        model.addAttribute("employees", employeeService.getAll());
        return "payroll/form";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        payrollService.delete(id);
        return "redirect:/payrolls/list";
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long id) {
        Payroll payroll = payrollService.findById(id);
        byte[] pdf = payrollService.generatePdf(payroll);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=payroll.pdf")
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(pdf);
    }

    // --- NEW: fetch attendance + shift/overtime dynamically ---
    @GetMapping("/attendance-data")
    @ResponseBody
    public Map<String, Object> getAttendanceData(@RequestParam Long empId,
                                                 @RequestParam String startDate,
                                                 @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<Attendance> records = attendanceService.getByEmployeeAndRange(empId, start, end);

        double totalOvertime = attendanceService.calculateOvertimeHours(empId, start, end);

        return Map.of(
                "records", records.stream().map(a -> Map.of(
                        "date", a.getDate().toString(),
                        "checkIn", a.getCheckIn() != null ? a.getCheckIn().toString() : "",
                        "checkOut", a.getCheckOut() != null ? a.getCheckOut().toString() : "",
                        "status", a.getStatus(),
                        "shiftHours", (a.getCheckIn() != null && a.getCheckOut() != null)
                                ? Duration.between(a.getCheckIn(), a.getCheckOut()).toHours()
                                : 0
                )).collect(Collectors.toList()),
                "totalOvertime", totalOvertime
        );
    }
}
