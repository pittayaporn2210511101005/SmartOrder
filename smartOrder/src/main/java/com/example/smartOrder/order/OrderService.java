package com.example.smartOrder.order;

import com.example.smartOrder.orderdetails.OrderDetailsService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsService orderDetailsService;

    public OrderService(
            OrderRepository orderRepository,
            OrderDetailsService orderDetailsService
    ) {
        this.orderRepository = orderRepository;
        this.orderDetailsService = orderDetailsService;
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

    // เพิ่มยอดขายย้อนหลัง mock
    @Transactional
    public Order createMockOrder(MockOrderRequest request) {
        if (request.getCreatedAt() == null) {
            throw new RuntimeException("กรุณาระบุวันที่ขายย้อนหลัง");
        }

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("กรุณาเลือกสินค้าอย่างน้อย 1 รายการ");
        }

        Order order = new Order();
        order.setCreatedAt(request.getCreatedAt());
        order.setTotalSell(0);
        order.setStatus("PENDING");
        order.setFailReason(null);

        Order savedOrder = orderRepository.save(order);

        for (MockOrderItemRequest item : request.getItems()) {
            if (item.getProductId() == null || item.getProductId().isBlank()) {
                throw new RuntimeException("ไม่พบรหัสสินค้า");
            }

            if (item.getQuantity() <= 0) {
                throw new RuntimeException("จำนวนสินค้าต้องมากกว่า 0");
            }

            String stockType = item.getStockType();

            if (stockType == null || stockType.isBlank()) {
                stockType = request.getStockType();
            }

            if (stockType == null || stockType.isBlank()) {
                stockType = "store";
            }

            orderDetailsService.createOrderDetail(
                    savedOrder.getId(),
                    item.getProductId(),
                    item.getQuantity(),
                    stockType
            );
        }

        return orderRepository.findById(savedOrder.getId())
                .orElseThrow(() -> new RuntimeException("ไม่พบออเดอร์"));
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