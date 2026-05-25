package com.example.smartOrder.stockHistory;

import com.example.smartOrder.products.Products;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockHistoryService {
    private final StockHistoryRepository stockHistoryRepository;

    public StockHistoryService(StockHistoryRepository stockHistoryRepository) {
        this.stockHistoryRepository = stockHistoryRepository;
    }

    public StockHistory recordStockChange(Products product, int quantityChange, String type) {
        StockHistory history = new StockHistory();
        history.setProduct(product);
        history.setQuantityChange(quantityChange);
        history.setType(type);
        history.setChangedDate(LocalDateTime.now());

        // ตั้งราคาก่อน/หลังขาย ให้ JSON ไม่เป็น null
        history.setOldCostPrice(product.getCostPrice());
        history.setOldSellPrice(product.getSellPrice());
        history.setNewCostPrice(product.getCostPrice());  // ถ้าไม่เปลี่ยน
        history.setNewSellPrice(product.getSellPrice());  // ถ้าไม่เปลี่ยน

        return stockHistoryRepository.save(history);
    }

    public List<StockHistory> getHistoryByProduct(String productId) {
        return stockHistoryRepository.findAllByProduct_IdOrderByChangedDateDesc(productId);
    }
}