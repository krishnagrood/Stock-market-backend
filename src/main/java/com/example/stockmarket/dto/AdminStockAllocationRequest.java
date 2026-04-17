package com.example.stockmarket.dto;

public class AdminStockAllocationRequest {

    private Long userId;
    private Long stockId;
    private int quantity;

    public AdminStockAllocationRequest() {
    }

    public AdminStockAllocationRequest(Long userId, Long stockId, int quantity) {
        this.userId = userId;
        this.stockId = stockId;
        this.quantity = quantity;
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
