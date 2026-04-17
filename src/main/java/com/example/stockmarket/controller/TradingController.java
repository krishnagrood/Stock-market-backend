package com.example.stockmarket.controller;

import com.example.stockmarket.service.TradingService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class TradingController {

    private final TradingService tradingService;

    public TradingController(TradingService tradingService) {
        this.tradingService = tradingService;
    }

    @PostMapping("/buy")
    public String buy(
            @RequestParam Long userId,
            @RequestParam Long stockId,
            @RequestParam int qty
    ) {
        return tradingService.buy(userId, stockId, qty);
    }

    @PostMapping("/sell")
    public String sell(
            @RequestParam Long userId,
            @RequestParam Long stockId,
            @RequestParam int qty
    ) {
        return tradingService.sell(userId, stockId, qty);
    }
}