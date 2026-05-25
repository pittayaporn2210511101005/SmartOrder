package com.example.smartOrder.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    // หาออเดอร์ตามช่วงวันที่
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
