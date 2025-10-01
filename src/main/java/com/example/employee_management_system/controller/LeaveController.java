package com.example.employee_management_system.controller;

import com.example.employee_management_system.entity.LeaveRequest;
import com.example.employee_management_system.entity.Employee;
import com.example.employee_management_system.service.LeaveService;
import com.example.employee_management_system.service.EmployeeService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/leaves")
public class LeaveController {

    private final LeaveService leaveService;
    private final EmployeeService employeeService;

    public LeaveController(LeaveService leaveService, EmployeeService employeeService) {
        this.leaveService = leaveService;
        this.employeeService = employeeService;
    }

    // list: /leaves or /leaves/list
    @GetMapping({"", "/list"})
    public String list(Model model, Authentication auth) {
        List<LeaveRequest> leaves = leaveService.findAll();
        List<Employee> employees = employeeService.getAll();

        // Precompute a map of employeeId -> fullName
        Map<Long, String> employeeNames = employees.stream()
                .collect(Collectors.toMap(
                        Employee::getId,
                        e -> e.getFirstName() + " " + e.getLastName()
                ));

        // Set fullName in each leave request (optional: can add a transient field in LeaveRequest)
        leaves.forEach(lr -> lr.setEmployeeName(employeeNames.getOrDefault(lr.getEmployeeId(), "Unknown")));

        model.addAttribute("leaves", leaves);
        return "leaves/list";
    }

    @GetMapping("/apply")
    public String applyForm(Model model) {
        model.addAttribute("leaveRequest", new LeaveRequest());
        model.addAttribute("employees", employeeService.getAll());
        return "leaves/form";
    }

    @PostMapping("/apply")
    public String apply(@ModelAttribute LeaveRequest lr) {
        // basic validation could be added (dates, overlaps)
        leaveService.apply(lr);
        return "redirect:/leaves/list";
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id, Authentication auth) {
        Long approverId = getCurrentUserId(auth);
        leaveService.decide(id, approverId, true);
        return "redirect:/leaves/list";
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id, Authentication auth) {
        Long approverId = getCurrentUserId(auth);
        leaveService.decide(id, approverId, false);
        return "redirect:/leaves/list";
    }

    // helper: derive current user id from Authentication (assumes username maps to user)
    private Long getCurrentUserId(Authentication auth) {
        if (auth == null) return 1L; // fallback demo admin id
        try {
            String username = auth.getName();
            // TODO: implement actual lookup by username to get user id
            return 1L;
        } catch (Exception ex) {
            return 1L;
        }
    }
}
