package com.example.stockmarket;

import com.example.stockmarket.entity.User;
import com.example.stockmarket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseReset implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== LISTING ALL ADMINS ===");
        List<User> admins = userRepository.findAll().stream().filter(u -> "ADMIN".equals(u.getRole())).toList();
        for (User admin : admins) {
            System.out.println("Admin Username: " + admin.getUsername() + ", Role: " + admin.getRole());
        }
        
        System.out.println("=== UPDATING RAHUL PASSWORD ===");
        userRepository.findByUsername("Rahul").ifPresent(user -> {
            user.setPassword(passwordEncoder.encode("Rahul$1711"));
            userRepository.save(user);
            System.out.println("Password for Rahul updated successfully.");
        });
        
        System.out.println("=== DONE ===");
    }
}
