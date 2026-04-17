package com.example.stockmarket.repository;

import com.example.stockmarket.entity.PriceUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceUpdateRepository extends JpaRepository<PriceUpdate, Long> {
}