package com.example.employee_management_system.entity;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="salary_record", uniqueConstraints = {@UniqueConstraint(columnNames = {"employee_id","month_year"})})
public class SalaryRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name="employee_id", nullable=false) private Long employeeId;
    @Column(name="month_year", nullable=false) private LocalDate monthYear; // store first day of month
    private BigDecimal basic;
    private BigDecimal allowances;
    private BigDecimal deductions;
    private BigDecimal netPay;
    private Long generatedBy;
    private LocalDateTime generatedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @PrePersist public void prePersist(){ generatedAt = LocalDateTime.now(); createdAt = LocalDateTime.now(); updatedAt = createdAt; }
    @PreUpdate public void preUpdate(){ updatedAt = LocalDateTime.now(); }
    // getters & setters
}
