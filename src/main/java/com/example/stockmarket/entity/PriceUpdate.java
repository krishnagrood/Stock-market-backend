package com.example.stockmarket.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "price_updates")
public class PriceUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long stockId;
    private String stockName;

    private double oldPrice;
    private double newPrice;

    // ================= DEFAULT CONSTRUCTOR =================
    public PriceUpdate() {
    }

    // ================= FULL CONSTRUCTOR =================
    public PriceUpdate(Long stockId, String stockName, double oldPrice, double newPrice) {
        this.stockId = stockId;
        this.stockName = stockName;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
    }

    // ================= GETTERS =================
    public Long getId() {
        return id;
    }

    public Long getStockId() {
        return stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public double getNewPrice() {
        return newPrice;
    }

    // ================= SETTERS =================
    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }
}