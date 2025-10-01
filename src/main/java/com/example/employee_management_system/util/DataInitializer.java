package com.example.employee_management_system.util;


import com.example.employee_management_system.entity.User;
import com.example.employee_management_system.entity.Employee;
import com.example.employee_management_system.repository.UserRepository;
import com.example.employee_management_system.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository userRepo, PasswordEncoder encoder, EmployeeRepository empRepo){
        return args -> {
            if(userRepo.findByUsername("admin").isEmpty()){
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole("ADMIN");
                userRepo.save(admin);
            }
            if(userRepo.findByUsername("hr").isEmpty()){
                User hr = new User();
                hr.setUsername("hr");
                hr.setPassword(encoder.encode("hr123"));
                hr.setRole("HR");
                userRepo.save(hr);
            }
            if(userRepo.findByUsername("emp1").isEmpty()){
                Employee e = new Employee();
                e.setFirstName("Kasun");
                e.setLastName("Perera");
                e.setEmail("kasuny@example.com");
                e.setHireDate(LocalDate.of(2023,1,5));
                e.setPosition("Developer");
                //e.setDepartment("IT");
                e.setSalary(new Double("65000"));
                empRepo.save(e);

                User empUser = new User();
                empUser.setUsername("emp1");
                empUser.setPassword(encoder.encode("emp123"));
                empUser.setRole("EMPLOYEE");
                //empUser.setEmployeeId(e.getId());
                userRepo.save(empUser);
            }
        };
    }
}

