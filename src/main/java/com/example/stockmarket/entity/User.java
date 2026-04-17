package com.example.stockmarket.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // ✅ DEFAULT BALANCE SET HERE
    private double balance = 500000;

    private String role;

    // ================= DEFAULT CONSTRUCTOR =================
    public User() {}

    // ================= MAIN CONSTRUCTOR (USED IN DATALOADER) =================
    public User(String username, String password, String role, double balance) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.balance = balance;
    }

    // ================= OPTIONAL CONSTRUCTOR =================
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.balance = 500000; // ✅ auto assign default
    }

    // ================= GETTERS =================
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public String getRole() {
        return role;
    }

    // ================= SETTERS =================
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setRole(String role) {
        this.role = role;
    }
}