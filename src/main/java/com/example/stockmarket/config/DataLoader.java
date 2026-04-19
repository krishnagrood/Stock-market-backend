package com.example.stockmarket.config;

import com.example.stockmarket.entity.Holding;
import com.example.stockmarket.entity.User;
import com.example.stockmarket.repository.HoldingRepository;
import com.example.stockmarket.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final HoldingRepository holdingRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository,
                      HoldingRepository holdingRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.holdingRepository = holdingRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (userRepository.count() == 0) {

            // =========================
            // CREATE 3 ADMINS
            // =========================
            userRepository.save(new User("Rahul", passwordEncoder.encode("Rahul@123"), "ADMIN", 500000));
            userRepository.save(new User("Boss", passwordEncoder.encode("Boss@123"), "ADMIN", 500000));
            userRepository.save(new User("Titanadmin", passwordEncoder.encode("Titan@123"), "ADMIN", 500000));

            // System created admins only. User accounts are now created manually via Admin Panel.

            System.out.println("✅ 3 admins created with ₹500000 balance.");
            System.out.println("✅ 20 users created with ₹500000 balance.");
        } else {
            System.out.println("✅ Users already exist. Skipping data seeding.");
        }
    }
}