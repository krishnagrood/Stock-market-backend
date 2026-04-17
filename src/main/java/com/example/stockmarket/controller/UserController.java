package com.example.stockmarket.controller;

import com.example.stockmarket.entity.User;
import com.example.stockmarket.repository.UserRepository;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")   // ✅ IMPORTANT
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ================= GET USER BY ID =================
    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}