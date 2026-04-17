package com.example.stockmarket.dto;

public class AdminOrderRequest {

    private String buyerTeam;
    private String sellerTeam;
    private String buyerUsername;
    private String sellerUsername;
    private String stockName;
    private double price;
    private int quantity;

    public AdminOrderRequest() {
    }

    public AdminOrderRequest(String buyerTeam,
                             String sellerTeam,
                             String buyerUsername,
                             String sellerUsername,
                             String stockName,
                             double price,
                             int quantity) {
        this.buyerTeam = buyerTeam;
        this.sellerTeam = sellerTeam;
        this.buyerUsername = buyerUsername;
        this.sellerUsername = sellerUsername;
        this.stockName = stockName;
        this.price = price;
        this.quantity = quantity;
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

    public String getBuyerUsername() {
        return buyerUsername;
    }

    public void setBuyerUsername(String buyerUsername) {
        this.buyerUsername = buyerUsername;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
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
}