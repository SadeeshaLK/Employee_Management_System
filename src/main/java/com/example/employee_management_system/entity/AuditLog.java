package com.example.employee_management_system.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="audit_log")
public class AuditLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private Long userId;
    private String action;
    private String entity;
    private Long entityId;
    @Column(length=2000) private String details;
    private LocalDateTime createdAt;
    @PrePersist public void prePersist(){ createdAt = LocalDateTime.now(); }
    // getters & setters
}

