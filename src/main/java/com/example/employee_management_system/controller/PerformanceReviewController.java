package com.example.employee_management_system.controller;

import com.example.employee_management_system.entity.Employee;
import com.example.employee_management_system.entity.PerformanceReview;
import com.example.employee_management_system.service.EmployeeService;
import com.example.employee_management_system.service.PerformanceReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reviews")
public class PerformanceReviewController {

    private final PerformanceReviewService reviewService;
    private final EmployeeService employeeService;

    public PerformanceReviewController(PerformanceReviewService reviewService, EmployeeService employeeService) {
        this.reviewService = reviewService;
        this.employeeService = employeeService;
    }

    // List all reviews
    @GetMapping({"", "/list"})
    public String listReviews(Model model) {
        List<PerformanceReview> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
        return "reviews/list";
    }

    // Show form to add a new review
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("performanceReview", new PerformanceReview());
        model.addAttribute("employees", employeeService.getAll());
        return "reviews/form";
    }

    // Show form to edit an existing review
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        PerformanceReview review = reviewService.getAllReviews()
                .stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID: " + id));
        model.addAttribute("performanceReview", review);
        model.addAttribute("employees", employeeService.getAll());
        return "reviews/form";
    }

    // Save new or updated review
    @PostMapping("/save")
    public String saveReview(@ModelAttribute PerformanceReview review) {
        Employee emp = employeeService.getById(review.getEmployee().getId());
        review.setEmployee(emp);
        reviewService.saveReview(review);
        return "redirect:/reviews/list";
    }

    // Delete a review
    @GetMapping("/{id}/delete")
    public String deleteReview(@PathVariable Long id) {
        reviewService.getAllReviews()
                .stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .ifPresent(r -> reviewService.getAllReviews().remove(r)); // simple remove, adjust for repo.deleteById
        return "redirect:/reviews/list";
    }
}
