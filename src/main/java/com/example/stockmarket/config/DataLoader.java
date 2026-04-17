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

        // Delete old holdings first
        holdingRepository.deleteAll();

        // Delete all users including admins
        userRepository.deleteAll();

        // =========================
        // CREATE 3 ADMINS
        // =========================
        userRepository.save(new User("Rahul", passwordEncoder.encode("Rahul@123"), "ADMIN", 500000));
        userRepository.save(new User("Boss", passwordEncoder.encode("Boss@123"), "ADMIN", 500000));
        userRepository.save(new User("Titanadmin", passwordEncoder.encode("Titan@123"), "ADMIN", 500000));

        // =========================
        // CREATE 20 USERS
        // =========================
        userRepository.save(new User("Blaze", passwordEncoder.encode("Blaze@123"), "USER", 500000));
        userRepository.save(new User("Nova", passwordEncoder.encode("Nova@123"), "USER", 500000));
        userRepository.save(new User("Ace", passwordEncoder.encode("Ace@123"), "USER", 500000));
        userRepository.save(new User("Storm", passwordEncoder.encode("Storm@123"), "USER", 500000));
        userRepository.save(new User("Falcon", passwordEncoder.encode("Falcon@123"), "USER", 500000));
        userRepository.save(new User("Shadow", passwordEncoder.encode("Shadow@123"), "USER", 500000));
        userRepository.save(new User("Phoenix", passwordEncoder.encode("Phoenix@123"), "USER", 500000));
        userRepository.save(new User("Viper", passwordEncoder.encode("Viper@123"), "USER", 500000));
        userRepository.save(new User("Titan", passwordEncoder.encode("TitanUser@123"), "USER", 500000));
        userRepository.save(new User("Rocket", passwordEncoder.encode("Rocket@123"), "USER", 500000));
        userRepository.save(new User("Zen", passwordEncoder.encode("Zen@123"), "USER", 500000));
        userRepository.save(new User("Spark", passwordEncoder.encode("Spark@123"), "USER", 500000));
        userRepository.save(new User("Alpha", passwordEncoder.encode("Alpha@123"), "USER", 500000));
        userRepository.save(new User("Neon", passwordEncoder.encode("Neon@123"), "USER", 500000));
        userRepository.save(new User("Orbit", passwordEncoder.encode("Orbit@123"), "USER", 500000));
        userRepository.save(new User("Comet", passwordEncoder.encode("Comet@123"), "USER", 500000));
        userRepository.save(new User("Drift", passwordEncoder.encode("Drift@123"), "USER", 500000));
        userRepository.save(new User("Frost", passwordEncoder.encode("Frost@123"), "USER", 500000));
        userRepository.save(new User("Glitch", passwordEncoder.encode("Glitch@123"), "USER", 500000));
        userRepository.save(new User("Havoc", passwordEncoder.encode("Havoc@123"), "USER", 500000));

        System.out.println("✅ All old holdings deleted.");
        System.out.println("✅ All old users deleted.");
        System.out.println("✅ 3 admins created with ₹500000 balance.");
        System.out.println("✅ 20 users created with ₹500000 balance.");
    }
}