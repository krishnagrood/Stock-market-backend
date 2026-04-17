package com.example.stockmarket.repository;

import com.example.stockmarket.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByNameIgnoreCase(String name);
}
