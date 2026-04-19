package com.example.stockmarket.service;

import com.example.stockmarket.entity.Stock;
import com.example.stockmarket.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableScheduling
public class StockPriceService {

    @Autowired StockRepository stockRepo;
    @Autowired SimpMessagingTemplate template;

    @Scheduled(fixedRate = 3000)
    public void updatePrices() {

        List<Stock> stocks = stockRepo.findAll();

        // Removed random price fluctuations. Prices will now ONLY be updated 
        // when the admin authorizes the orders.
        // We still broadcast the current prices so all users are in perfect sync.

        template.convertAndSend("/topic/stocks", stocks);
    }
}