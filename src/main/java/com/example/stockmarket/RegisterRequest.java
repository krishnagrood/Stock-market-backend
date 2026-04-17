package com.example.stockmarket;

public class RegisterRequest {

    private String username;
    private String password;
    private double balance;

    // ✅ No-args constructor
    public RegisterRequest() {}

    // ✅ Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    // ✅ Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}