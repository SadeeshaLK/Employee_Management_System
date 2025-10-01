package com.example.employee_management_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String jobTitle;
    private String position;




    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    private LocalDate hireDate;
    private Double salary;
}
