package com.example.stockmarket.repository;

import com.example.stockmarket.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    // ✅ Find all stocks owned by a user
    List<Portfolio> findByUserId(Long userId);
}