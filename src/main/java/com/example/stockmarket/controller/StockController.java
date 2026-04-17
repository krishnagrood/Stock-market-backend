package com.example.stockmarket.controller;

import com.example.stockmarket.config.TradingState;
import com.example.stockmarket.entity.Holding;
import com.example.stockmarket.entity.Stock;
import com.example.stockmarket.entity.User;
import com.example.stockmarket.repository.HoldingRepository;
import com.example.stockmarket.repository.StockRepository;
import com.example.stockmarket.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class StockController {

    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final HoldingRepository holdingRepository;

    public StockController(StockRepository stockRepository,
                           UserRepository userRepository,
                           HoldingRepository holdingRepository) {
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
        this.holdingRepository = holdingRepository;
    }

    // ================= GET ALL STOCKS =================
    @GetMapping("/stocks")
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    // ================= BUY STOCK =================
    @PostMapping("/stocks/buy")
    public String buyStock(@RequestParam Long userId,
                           @RequestParam Long stockId,
                           @RequestParam int qty) {

        if (!TradingState.isTradingOpen()) {
            throw new RuntimeException("Trading is currently CLOSED!");
        }

        if (qty <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        double totalPrice = stock.getPrice() * qty;

        if (user.getBalance() < totalPrice) {
            throw new RuntimeException("Insufficient balance");
        }

        user.setBalance(user.getBalance() - totalPrice);
        userRepository.save(user);

        Holding holding = holdingRepository.findByUserIdAndStockId(userId, stockId)
                .orElse(new Holding(userId, stockId, 0));

        holding.setQuantity(holding.getQuantity() + qty);
        holdingRepository.save(holding);

        return "Stock bought successfully!";
    }

    // ================= SELL STOCK =================
    @PostMapping("/stocks/sell")
    public String sellStock(@RequestParam Long userId,
                            @RequestParam Long stockId,
                            @RequestParam int qty) {

        if (!TradingState.isTradingOpen()) {
            throw new RuntimeException("Trading is currently CLOSED!");
        }

        if (qty <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        Holding holding = holdingRepository.findByUserIdAndStockId(userId, stockId)
                .orElseThrow(() -> new RuntimeException("No stock owned"));

        if (holding.getQuantity() < qty) {
            throw new RuntimeException("Not enough stock to sell");
        }

        holding.setQuantity(holding.getQuantity() - qty);

        if (holding.getQuantity() == 0) {
            holdingRepository.delete(holding);
        } else {
            holdingRepository.save(holding);
        }

        double totalPrice = stock.getPrice() * qty;
        user.setBalance(user.getBalance() + totalPrice);
        userRepository.save(user);

        return "Stock sold successfully!";
    }
}