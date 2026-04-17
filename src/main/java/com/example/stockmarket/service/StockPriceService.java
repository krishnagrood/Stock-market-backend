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

        for(Stock s : stocks){
            s.setPrice(s.getPrice() + (Math.random()*20 - 10));
            stockRepo.save(s);
        }

        template.convertAndSend("/topic/stocks", stocks);
    }
}