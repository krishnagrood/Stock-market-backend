package com.example.stockmarket.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "portfolio")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long stockId;
    private int quantity;

    // Default constructor (IMPORTANT)
    public Portfolio() {}

    // Constructor (useful for buy logic)
    public Portfolio(Long userId, Long stockId, int quantity) {
        this.userId = userId;
        this.stockId = stockId;
        this.quantity = quantity;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getStockId() {
        return stockId;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}