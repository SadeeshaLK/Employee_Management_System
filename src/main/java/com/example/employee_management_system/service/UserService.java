package com.example.employee_management_system.service;

import com.example.employee_management_system.entity.User;
import com.example.employee_management_system.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo){ this.repo = repo; }
    public Optional<User> findByUsername(String username){ return repo.findByUsername(username); }
    public User save(User u){ return repo.save(u); }
}
