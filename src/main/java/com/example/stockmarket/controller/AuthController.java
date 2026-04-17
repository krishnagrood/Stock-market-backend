package com.example.stockmarket.controller;

import com.example.stockmarket.entity.User;
import com.example.stockmarket.repository.UserRepository;
import com.example.stockmarket.LoginRequest;
import com.example.stockmarket.RegisterRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")   // ✅ IMPORTANT: all APIs start with /api
public class AuthController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        System.out.println("Register attempt: " + request.getUsername());

        // ✅ Check if user already exists
        if (repo.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("Username already exists");
        }

        // ✅ Create new user
        User user = new User();
        user.setUsername(request.getUsername());

        // 🔐 Encode password
        user.setPassword(encoder.encode(request.getPassword()));

        // 💰 Set balance
        if (request.getBalance() > 0) {
            user.setBalance(request.getBalance());
        } else {
            user.setBalance(10000); // default balance
        }

        // 👤 Set default role
        user.setRole("USER");

        // Save user
        repo.save(user);

        System.out.println("User registered successfully with balance: " + user.getBalance());

        return ResponseEntity.ok(user);
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        System.out.println("Login attempt: " + request.getUsername());

        User user = repo.findByUsername(request.getUsername()).orElse(null);

        // ❌ User not found
        if (user == null) {
            System.out.println("User NOT found");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }

        // ❌ Password mismatch
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            System.out.println("Password mismatch");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }

        // ✅ Success
        System.out.println("Login SUCCESS");

        return ResponseEntity.ok(user);
    }
}