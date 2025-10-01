package com.example.employee_management_system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="customer")
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String name;
    private String email;
    private String phone;
    private String organization;
    private LocalDateTime createdAt;
    @PrePersist public void prePersist(){ createdAt = LocalDateTime.now(); }
    // getters & setters
}

