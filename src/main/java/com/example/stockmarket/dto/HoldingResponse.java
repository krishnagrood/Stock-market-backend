package com.example.stockmarket.dto;

public class HoldingResponse {
    private Long holdingId;
    private Long userId;
    private Long stockId;
    private String stockName;
    private double currentPrice;
    private int quantity;
    private double totalValue;

    public HoldingResponse() {}

    public HoldingResponse(Long holdingId, Long userId, Long stockId, String stockName,
                           double currentPrice, int quantity, double totalValue) {
        this.holdingId = holdingId;
        this.userId = userId;
        this.stockId = stockId;
        this.stockName = stockName;
        this.currentPrice = currentPrice;
        this.quantity = quantity;
        this.totalValue = totalValue;
    }

    public Long getHoldingId() {
        return holdingId;
    }

    public void setHoldingId(Long holdingId) {
        this.holdingId = holdingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }
}