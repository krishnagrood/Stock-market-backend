package com.example.stockmarket.dto;

public class HoldingResponse {
    private Long holdingId;
    private Long userId;
    private Long stockId;
    private String stockName;
    private double currentPrice;
    private int quantity;
    private double totalValue;
    private double totalInvestment;
    private double avgBuyPrice;
    private double pnl;
    private double pnlPercent;

    public HoldingResponse() {}

    public HoldingResponse(Long holdingId, Long userId, Long stockId, String stockName,
                           double currentPrice, int quantity, double totalValue,
                           double totalInvestment) {
        this.holdingId = holdingId;
        this.userId = userId;
        this.stockId = stockId;
        this.stockName = stockName;
        this.currentPrice = currentPrice;
        this.quantity = quantity;
        this.totalValue = totalValue;
        this.totalInvestment = totalInvestment;
        this.avgBuyPrice = quantity > 0 ? totalInvestment / quantity : 0;
        this.pnl = totalValue - totalInvestment;
        this.pnlPercent = totalInvestment > 0 ? ((totalValue - totalInvestment) / totalInvestment) * 100 : 0;
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

    public double getTotalInvestment() {
        return totalInvestment;
    }

    public void setTotalInvestment(double totalInvestment) {
        this.totalInvestment = totalInvestment;
    }

    public double getAvgBuyPrice() {
        return avgBuyPrice;
    }

    public void setAvgBuyPrice(double avgBuyPrice) {
        this.avgBuyPrice = avgBuyPrice;
    }

    public double getPnl() {
        return pnl;
    }

    public void setPnl(double pnl) {
        this.pnl = pnl;
    }

    public double getPnlPercent() {
        return pnlPercent;
    }

    public void setPnlPercent(double pnlPercent) {
        this.pnlPercent = pnlPercent;
    }
}