package com.example.stockmarket.service;

import com.example.stockmarket.entity.AdminOrder;
import com.example.stockmarket.entity.Stock;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketBrainService {

    public double calculateNewPrice(Stock stock, List<AdminOrder> stockOrders) {
        if (stockOrders == null || stockOrders.isEmpty()) {
            return stock.getPrice();
        }

        double totalValue = 0;
        int totalQuantity = 0;

        for (AdminOrder order : stockOrders) {
            totalValue += order.getOrderValue();
            totalQuantity += order.getQuantity();
        }

        double currentPrice = stock.getPrice();
        if (currentPrice <= 0) {
            currentPrice = 1.0; // Fail-safe
        }
        
        double vwap = totalQuantity == 0 ? currentPrice : (totalValue / totalQuantity);

        // 1. Order Imbalance (Sentiment)
        // Positive if orders are priced higher than current market, negative if lower.
        double orderImbalance = (vwap - currentPrice) / currentPrice;

        // 2. Sentiment Multiplier
        // Amplifies the trend slightly (1.5x factor).
        double sentimentMultiplier = 1.0 + (orderImbalance * 1.5);

        // 3. Liquidity Weight
        // High volume translates to a higher weight. Assuming 1000 shares is full liquidity weighting.
        double liquidityWeight = Math.min((double) totalQuantity / 1000.0, 1.0);

        // Final Price Calculation
        double newPrice = currentPrice + ((vwap - currentPrice) * liquidityWeight * sentimentMultiplier);

        // 4. Circuit Breaker: Limit variation to max 15% up or down
        double maxChange = currentPrice * 0.15;
        double minPrice = currentPrice - maxChange;
        double maxPrice = currentPrice + maxChange;

        // Clamp the price between minPrice and maxPrice
        newPrice = Math.max(minPrice, Math.min(newPrice, maxPrice));

        // Sanity Check: Ensure price is never negative and has a minimum floor of 0.01
        return Math.max(newPrice, 0.01);
    }
}
