package com.example.employee_management_system.controller;

import com.example.employee_management_system.entity.Employee;
import com.example.employee_management_system.service.DepartmentService;
import com.example.employee_management_system.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired // ✅ Autowire the DepartmentService to avoid NullPointerException
    private DepartmentService departmentService;

    // Show list of employees
    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAll());
        return "employees/list"; // templates/employees/list.html
    }

    // Show form to add new employee
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentService.getAllDepartments()); // populate dropdown
        return "employees/form"; // templates/employees/form.html
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
            employee.setDepartment(departmentService.getById(employee.getDepartment().getId()));
        }
        employeeService.saveEmployee(employee);
        return "redirect:/employees";
    }

    // Edit employee
    @GetMapping("/edit/{id}")
    public String editEmployee(@PathVariable("id") Long id, Model model) {
        Employee employee = employeeService.getById(id);
        if (employee == null) {
            return "redirect:/employees"; // fallback if not found
        }
        model.addAttribute("employee", employee);
        model.addAttribute("departments", departmentService.getAllDepartments()); // populate dropdown
        return "employees/form"; // reuse same form for edit
    }

    // Delete employee
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteById(id);
        return "redirect:/employees";
    }
}
