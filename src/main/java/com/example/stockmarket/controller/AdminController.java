package com.example.stockmarket.controller;

import com.example.stockmarket.config.TradingState;
import com.example.stockmarket.dto.AdminStockAllocationRequest;
import com.example.stockmarket.entity.Holding;
import com.example.stockmarket.entity.Stock;
import com.example.stockmarket.entity.User;
import com.example.stockmarket.repository.HoldingRepository;
import com.example.stockmarket.repository.StockRepository;
import com.example.stockmarket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HoldingRepository holdingRepository;

    @GetMapping("/trading-status")
    public boolean getTradingStatus() {
        return TradingState.isTradingOpen();
    }

    @PostMapping("/admin/start")
    public Map<String, Object> startTrading() {
        Map<String, Object> response = new HashMap<>();
        TradingState.setTradingOpen(true);
        response.put("success", true);
        response.put("message", "Trading started successfully");
        return response;
    }

    @PostMapping("/admin/stop")
    public Map<String, Object> stopTrading() {
        Map<String, Object> response = new HashMap<>();
        TradingState.setTradingOpen(false);
        response.put("success", true);
        response.put("message", "Trading stopped successfully");
        return response;
    }

    @PutMapping("/admin/updatePrice")
    public Map<String, Object> updatePrice(@RequestParam Long stockId,
                                           @RequestParam double price) {
        Map<String, Object> response = new HashMap<>();

        if (price <= 0) {
            response.put("success", false);
            response.put("message", "Price must be greater than 0");
            return response;
        }

        Optional<Stock> stockOptional = stockRepository.findById(stockId);

        if (stockOptional.isEmpty()) {
            response.put("success", false);
            response.put("message", "Stock not found");
            return response;
        }

        Stock stock = stockOptional.get();
        stock.setPreviousPrice(stock.getPrice());
        stock.setPrice(price);
        stockRepository.save(stock);

        response.put("success", true);
        response.put("message", "Stock price updated successfully");
        return response;
    }

    @PostMapping("/admin/addStock")
    public Map<String, Object> addStock(@RequestBody Stock stockRequest) {
        Map<String, Object> response = new HashMap<>();

        if (stockRequest.getName() == null || stockRequest.getName().trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Stock name is required");
            return response;
        }

        if (stockRequest.getPrice() <= 0) {
            response.put("success", false);
            response.put("message", "Stock price must be greater than 0");
            return response;
        }

        Optional<Stock> existingStock = stockRepository.findByNameIgnoreCase(stockRequest.getName());

        if (existingStock.isPresent()) {
            response.put("success", false);
            response.put("message", "Stock with this name already exists");
            return response;
        }

        Stock stock = new Stock();
        stock.setName(stockRequest.getName().trim());
        stock.setPrice(stockRequest.getPrice());
        stock.setPreviousPrice(stockRequest.getPrice());

        stockRepository.save(stock);

        response.put("success", true);
        response.put("message", "Stock added successfully");
        response.put("stock", stock);
        return response;
    }

    @DeleteMapping("/admin/deleteStock")
    public Map<String, Object> deleteStock(@RequestParam Long stockId) {
        Map<String, Object> response = new HashMap<>();

        Optional<Stock> stockOptional = stockRepository.findById(stockId);

        if (stockOptional.isEmpty()) {
            response.put("success", false);
            response.put("message", "Stock not found");
            return response;
        }

        stockRepository.deleteById(stockId);

        response.put("success", true);
        response.put("message", "Stock deleted successfully");
        return response;
    }

    @GetMapping("/admin/users")
    public List<Map<String, Object>> getAllUsersForAdmin() {
        return userRepository.findAll()
                .stream()
                .filter(user -> "USER".equalsIgnoreCase(user.getRole()))
                .map(user -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", user.getId());
                    data.put("username", user.getUsername());
                    data.put("balance", user.getBalance());
                    data.put("role", user.getRole());
                    return data;
                })
                .collect(Collectors.toList());
    }

    @PostMapping("/admin/allocateStock")
    public Map<String, Object> allocateStockToUser(@RequestBody AdminStockAllocationRequest request) {
        Map<String, Object> response = new HashMap<>();

        if (request.getUserId() == null) {
            response.put("success", false);
            response.put("message", "User is required");
            return response;
        }

        if (request.getStockId() == null) {
            response.put("success", false);
            response.put("message", "Stock is required");
            return response;
        }

        if (request.getQuantity() <= 0) {
            response.put("success", false);
            response.put("message", "Quantity must be greater than 0");
            return response;
        }

        Optional<User> userOptional = userRepository.findById(request.getUserId());
        if (userOptional.isEmpty()) {
            response.put("success", false);
            response.put("message", "User not found");
            return response;
        }

        Optional<Stock> stockOptional = stockRepository.findById(request.getStockId());
        if (stockOptional.isEmpty()) {
            response.put("success", false);
            response.put("message", "Stock not found");
            return response;
        }

        User user = userOptional.get();
        Stock stock = stockOptional.get();

        double totalCost = stock.getPrice() * request.getQuantity();

        if (user.getBalance() < totalCost) {
            response.put("success", false);
            response.put("message", "Insufficient user balance");
            response.put("requiredAmount", totalCost);
            response.put("availableBalance", user.getBalance());
            return response;
        }

        Holding holding = holdingRepository.findByUserIdAndStockId(user.getId(), stock.getId())
                .orElse(new Holding(user.getId(), stock.getId(), 0));

        holding.setQuantity(holding.getQuantity() + request.getQuantity());
        holdingRepository.save(holding);

        user.setBalance(user.getBalance() - totalCost);
        userRepository.save(user);

        response.put("success", true);
        response.put("message", "Stock allocated successfully");
        response.put("username", user.getUsername());
        response.put("stockName", stock.getName());
        response.put("allocatedQuantity", request.getQuantity());
        response.put("deductedAmount", totalCost);
        response.put("remainingBalance", user.getBalance());

        return response;
    }
}