package com.example.stockmarket.service;

import com.example.stockmarket.entity.*;
import com.example.stockmarket.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    // ✅ BUY STOCK
    public String buy(Long userId, Long stockId, int qty) {

        User user = userRepository.findById(userId).orElseThrow();
        Stock stock = stockRepository.findById(stockId).orElseThrow();

        double totalPrice = stock.getPrice() * qty;

        if (user.getBalance() < totalPrice) {
            return "Not enough balance";
        }

        // Deduct balance
        user.setBalance(user.getBalance() - totalPrice);
        userRepository.save(user);

        // Update portfolio
        Portfolio portfolio = portfolioRepository
                .findByUserId(userId)
                .stream()
                .filter(p -> p.getStockId().equals(stockId))
                .findFirst()
                .orElse(null);

        if (portfolio == null) {
            portfolio = new Portfolio(userId, stockId, qty);
        } else {
            portfolio.setQuantity(portfolio.getQuantity() + qty);
        }

        portfolioRepository.save(portfolio);

        return "Stock bought successfully";
    }

    // ✅ SELL STOCK
    public String sell(Long userId, Long stockId, int qty) {

        User user = userRepository.findById(userId).orElseThrow();
        Stock stock = stockRepository.findById(stockId).orElseThrow();

        Portfolio portfolio = portfolioRepository
                .findByUserId(userId)
                .stream()
                .filter(p -> p.getStockId().equals(stockId))
                .findFirst()
                .orElse(null);

        if (portfolio == null || portfolio.getQuantity() < qty) {
            return "Not enough stock to sell";
        }

        // Reduce quantity
        portfolio.setQuantity(portfolio.getQuantity() - qty);
        portfolioRepository.save(portfolio);

        // Add balance
        double totalPrice = stock.getPrice() * qty;
        user.setBalance(user.getBalance() + totalPrice);
        userRepository.save(user);

        return "Stock sold successfully";
    }
}