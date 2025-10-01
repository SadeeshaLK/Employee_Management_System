package com.example.employee_management_system.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="feedback")
public class Feedback {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private Long customerId;
    private Long employeeId;
    @Column(length=2000) private String message;
    private Integer rating;
    private LocalDateTime createdAt;
    @PrePersist public void prePersist(){ createdAt = LocalDateTime.now(); }
    // getters & setters
}
