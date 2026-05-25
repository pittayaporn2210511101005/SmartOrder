package com.example.smartOrder.stockHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockHistoryRepository extends JpaRepository<StockHistory, Integer> {

    List<StockHistory> findAllByProduct_IdOrderByChangedDateDesc(String productId);
}
