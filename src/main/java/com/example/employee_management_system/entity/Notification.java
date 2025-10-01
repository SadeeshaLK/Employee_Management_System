package com.example.employee_management_system.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="notification")
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private Long userId;
    private String title;
    @Column(length=2000) private String body;
    private String url;
    private Boolean isRead = false;
    private LocalDateTime createdAt;
    @PrePersist public void prePersist(){ createdAt = LocalDateTime.now(); }
    // getters & setters
}
