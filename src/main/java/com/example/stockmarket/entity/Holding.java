package com.example.stockmarket.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "holding")
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long stockId;
    private int quantity;
    private double totalInvestment;

    public Holding() {
    }

    public Holding(Long userId, Long stockId, int quantity) {
        this.userId = userId;
        this.stockId = stockId;
        this.quantity = quantity;
        this.totalInvestment = 0;
    }

    public Holding(Long userId, Long stockId, int quantity, double totalInvestment) {
        this.userId = userId;
        this.stockId = stockId;
        this.quantity = quantity;
        this.totalInvestment = totalInvestment;
    }

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

    public double getTotalInvestment() {
        return totalInvestment;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTotalInvestment(double totalInvestment) {
        this.totalInvestment = totalInvestment;
    }
}