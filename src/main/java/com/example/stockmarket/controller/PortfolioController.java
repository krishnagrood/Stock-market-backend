package com.example.stockmarket.controller;

import com.example.stockmarket.entity.Portfolio;
import com.example.stockmarket.repository.PortfolioRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/holdings")
@CrossOrigin(origins = "*")
public class PortfolioController {

    private final PortfolioRepository portfolioRepository;

    public PortfolioController(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    // ✅ Get all holdings for a user
    @GetMapping("/{userId}")
    public List<Portfolio> getHoldings(@PathVariable Long userId) {
        return portfolioRepository.findByUserId(userId);
    }
}