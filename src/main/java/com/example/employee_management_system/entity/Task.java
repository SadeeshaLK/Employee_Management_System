package com.example.employee_management_system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name="task")
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String title;
    @Column(length=2000) private String description;
    private Long assignerId;
    private Long assigneeId; // employee_id
    private String department;
    private String priority; // LOW,MEDIUM,HIGH
    private String status; // PENDING,IN_PROGRESS,COMPLETED,BLOCKED
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @PrePersist public void prePersist(){ createdAt = LocalDateTime.now(); updatedAt = createdAt; }
    @PreUpdate public void preUpdate(){ updatedAt = LocalDateTime.now(); }
    // getters & setters
}
