package com.example.stockmarket.service;

import com.example.stockmarket.entity.AdminOrder;
import com.example.stockmarket.entity.Stock;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketBrainService {

    /**
     * Intelligent price discovery engine.
     *
     * The "Brain" builds a master data picture from ALL orders received,
     * then determines a realistic new price by blending:
     *   1. Price Inertia    — The current market price carries heavy weight (the market
     *                         doesn't jump just because a few orders came in).
     *   2. Order Pressure   — The VWAP of orders nudges the price, but only proportionally
     *                         to the volume traded (liquidity impact).
     *   3. Sentiment Score  — If orders are consistently above or below the current price,
     *                         a small sentiment premium/discount is applied.
     *   4. Circuit Breaker  — Final price is hard-capped at ±15% from the previous price.
     *   5. Precision        — Result rounded to 2 decimal places.
     */
    public double calculateNewPrice(Stock stock, List<AdminOrder> stockOrders) {
        if (stockOrders == null || stockOrders.isEmpty()) {
            return stock.getPrice();
        }

        // ── Step 1: Build Master Data from all orders ──────────────────────
        double totalValue = 0;
        int totalQuantity = 0;
        int ordersAboveMarket = 0;
        int ordersBelowMarket = 0;

        double currentPrice = stock.getPrice();
        if (currentPrice <= 0) {
            currentPrice = 1.0; // Fail-safe
        }

        for (AdminOrder order : stockOrders) {
            totalValue += order.getOrderValue();
            totalQuantity += order.getQuantity();

            // Track sentiment: how many orders are above vs below current price
            if (order.getPrice() > currentPrice) {
                ordersAboveMarket++;
            } else if (order.getPrice() < currentPrice) {
                ordersBelowMarket++;
            }
        }

        double vwap = totalQuantity == 0 ? currentPrice : (totalValue / totalQuantity);

        // ── Step 2: Price Influence Scaling — how fast the market moves ─────
        // Reduced the inertia so the market reacts more aggressively to volume.
        // Even 50 units traded will now have a 50% impact on the new price.
        double orderInfluence = Math.min((double) totalQuantity / 100.0, 0.65);
        double priceInertia = 1.0 - orderInfluence;
        
        // Blended price: allows VWAP to pull the price much more strongly
        double blendedPrice = (currentPrice * priceInertia) + (vwap * orderInfluence);

        // ── Step 3: Sentiment Score — directionally aggressive nudge ───────
        // Measures overall market mood from the order book.
        int totalOrders = stockOrders.size();
        double sentimentScore = 0.0;
        if (totalOrders > 0) {
            sentimentScore = (double) (ordersAboveMarket - ordersBelowMarket) / totalOrders;
        }

        // Apply a more aggressive sentiment nudge (max ±8% for strong sentiment)
        // This ensures the 2-5% "real feel" variation the user requested.
        double sentimentNudge = sentimentScore * 0.08;
        double newPrice = blendedPrice * (1.0 + sentimentNudge);

        // ── Step 4: Circuit Breaker — cap at ±15% ─────────────────────────
        double maxChange = currentPrice * 0.15;
        double minPrice = currentPrice - maxChange;
        double maxPrice = currentPrice + maxChange;
        newPrice = Math.max(minPrice, Math.min(newPrice, maxPrice));

        // ── Step 5: Floor + Round to 2 decimal places ─────────────────────
        newPrice = Math.max(newPrice, 0.01);
        newPrice = Math.round(newPrice * 100.0) / 100.0;

        return newPrice;
    }
}
