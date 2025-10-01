package com.example.employee_management_system.service.impl;

import com.example.employee_management_system.entity.PerformanceReview;
import com.example.employee_management_system.repository.PerformanceReviewRepository;
import com.example.employee_management_system.service.PerformanceReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerformanceReviewServiceImpl implements PerformanceReviewService {

    private final PerformanceReviewRepository repository;

    public PerformanceReviewServiceImpl(PerformanceReviewRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PerformanceReview> getAllReviews() {
        return repository.findAll();
    }

    @Override
    public void saveReview(PerformanceReview review) {
        repository.save(review);
    }
}
