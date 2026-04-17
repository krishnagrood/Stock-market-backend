package com.example.stockmarket.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "admin_orders")
public class AdminOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String buyerTeam;
    private String sellerTeam;

    private Long buyerUserId;
    private Long sellerUserId;

    private String stockName;
    private double price;
    private int quantity;
    private double orderValue;

    public AdminOrder() {
    }

    public AdminOrder(String buyerTeam,
                      String sellerTeam,
                      Long buyerUserId,
                      Long sellerUserId,
                      String stockName,
                      double price,
                      int quantity,
                      double orderValue) {
        this.buyerTeam = buyerTeam;
        this.sellerTeam = sellerTeam;
        this.buyerUserId = buyerUserId;
        this.sellerUserId = sellerUserId;
        this.stockName = stockName;
        this.price = price;
        this.quantity = quantity;
        this.orderValue = orderValue;
    }

    public Long getId() {
        return id;
    }

    public String getBuyerTeam() {
        return buyerTeam;
    }

    public void setBuyerTeam(String buyerTeam) {
        this.buyerTeam = buyerTeam;
    }

    public String getSellerTeam() {
        return sellerTeam;
    }

    public void setSellerTeam(String sellerTeam) {
        this.sellerTeam = sellerTeam;
    }

    public Long getBuyerUserId() {
        return buyerUserId;
    }

    public void setBuyerUserId(Long buyerUserId) {
        this.buyerUserId = buyerUserId;
    }

    public Long getSellerUserId() {
        return sellerUserId;
    }

    public void setSellerUserId(Long sellerUserId) {
        this.sellerUserId = sellerUserId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(double orderValue) {
        this.orderValue = orderValue;
    }
}