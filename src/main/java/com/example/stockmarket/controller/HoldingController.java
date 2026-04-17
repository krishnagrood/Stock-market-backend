package com.example.stockmarket.controller;

import com.example.stockmarket.dto.HoldingResponse;
import com.example.stockmarket.entity.Holding;
import com.example.stockmarket.entity.Stock;
import com.example.stockmarket.repository.HoldingRepository;
import com.example.stockmarket.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/holdings")
@CrossOrigin(origins = "*")
public class HoldingController {

    @Autowired
    private HoldingRepository holdingRepository;

    @Autowired
    private StockRepository stockRepository;

    @GetMapping("/{userId}")
    public List<HoldingResponse> getHoldingsByUserId(@PathVariable Long userId) {
        List<Holding> holdings = holdingRepository.findByUserId(userId);
        List<HoldingResponse> response = new ArrayList<>();

        for (Holding holding : holdings) {
            Optional<Stock> stockOptional = stockRepository.findById(holding.getStockId());

            String stockName = "Unknown";
            double currentPrice = 0.0;

            if (stockOptional.isPresent()) {
                Stock stock = stockOptional.get();
                stockName = stock.getName();
                currentPrice = stock.getPrice();
            }

            double totalValue = currentPrice * holding.getQuantity();

            response.add(new HoldingResponse(
                    holding.getId(),
                    holding.getUserId(),
                    holding.getStockId(),
                    stockName,
                    currentPrice,
                    holding.getQuantity(),
                    totalValue
            ));
        }

        return response;
    }
}