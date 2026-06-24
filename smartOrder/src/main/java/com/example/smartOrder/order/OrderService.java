package com.example.smartOrder.order;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // เพิ่มออเดอร์
    public Order createOrder(Order order) {
        if (order.getTotalSell() < 0) {
            throw new RuntimeException("ยอดขายรวมต้องไม่ติดลบ");
        }
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalSell(0);
        order.setStatus("PENDING");
        order.setFailReason(null);
        return orderRepository.save(order);
    }

    // ดูออเดอร์ทั้งหมด
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // ดูออเดอร์ตาม ID
    public Order getOrderById(Integer id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบออเดอร์"));
    }

    // ดูออเดอร์ตามวันที่
    public List<Order> getOrdersByDate(LocalDate date) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        return orderRepository.findByCreatedAtBetween(start, end);
    }

    // แก้ไขยอดรวมออเดอร์
    public Order updateOrder(Integer id, Order order) {

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบออเดอร์"));

        if (order.getTotalSell() < 0) {
            throw new RuntimeException("ยอดขายรวมต้องไม่ติดลบ");
        }

        existingOrder.setTotalSell(order.getTotalSell());

        return orderRepository.save(existingOrder);
    }

    // อัปเดตออเดอร์เป็นทำรายการสำเร็จ
    public Order markOrderSuccess(Integer orderId, int totalSell) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("ไม่พบออเดอร์"));

        order.setStatus("SUCCESS");
        order.setFailReason(null);
        order.setTotalSell(totalSell);

        return orderRepository.save(order);
    }

    // อัปเดตออเดอร์เป็นทำรายการไม่สำเร็จ
    public Order markOrderFailed(Integer orderId, String reason) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("ไม่พบออเดอร์"));

        order.setStatus("FAILED");
        order.setFailReason(reason);
        order.setTotalSell(0);

        return orderRepository.save(order);
    }

    // ลบออเดอร์
    public void deleteOrder(Integer id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบออเดอร์"));

        orderRepository.delete(order);
    }
}