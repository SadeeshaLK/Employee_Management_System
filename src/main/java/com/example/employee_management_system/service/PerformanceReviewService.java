package com.example.employee_management_system.service;

import com.example.employee_management_system.entity.PerformanceReview;

import java.util.List;

public interface PerformanceReviewService {
    List<PerformanceReview> getAllReviews();
    void saveReview(PerformanceReview review);
}
