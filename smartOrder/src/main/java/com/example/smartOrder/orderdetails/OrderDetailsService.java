package com.example.smartOrder.orderdetails;

import com.example.smartOrder.order.Order;
import com.example.smartOrder.order.OrderRepository;
import com.example.smartOrder.products.ProductRepository;
import com.example.smartOrder.products.Products;
import com.example.smartOrder.stockHistory.StockHistoryService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StockHistoryService stockHistoryService;

    public OrderDetailsService(OrderDetailsRepository orderDetailsRepository,
                               OrderRepository orderRepository,
                               ProductRepository productRepository,
                               StockHistoryService stockHistoryService) {
        this.orderDetailsRepository = orderDetailsRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.stockHistoryService = stockHistoryService;
    }

    // เพิ่มรายการสินค้าเข้าออเดอร์
    public OrderDetails createOrderDetail(Integer orderId, String productId, int quantity) {

        if (quantity <= 0) {
            throw new RuntimeException("จำนวนสินค้าต้องมากกว่า 0");
        }

        // หา Order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("ไม่พบออเดอร์"));

        // หา Product
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("ไม่พบสินค้า"));

        // ตรวจสอบ stock
        if (product.getStock() < quantity) {
            throw new RuntimeException("สินค้า " + product.getProductName() + " มีไม่เพียงพอ");
        }

        // ลด stock
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

        // บันทึก StockHistory
        stockHistoryService.recordStockChange(product, -quantity, "SALE");

        // คำนวณราคา
        BigDecimal sellingPrice = product.getSellPrice();
        BigDecimal totalPrice = sellingPrice.multiply(BigDecimal.valueOf(quantity));

        // สร้าง OrderDetails
        OrderDetails detail = new OrderDetails();
        detail.setOrder(order);
        detail.setProduct(product);
        detail.setQuantity(quantity);
        detail.setSellingPrice(sellingPrice);
        detail.setTotalPrice(totalPrice);

        OrderDetails savedDetail = orderDetailsRepository.save(detail);

        // อัปเดต totalSell ของ Order
        order.setTotalSell(order.getTotalSell() + totalPrice.intValue());
        orderRepository.save(order);

        return savedDetail;
    }

    // ดูรายละเอียดสินค้าทั้งหมดในออเดอร์นั้น
    public List<OrderDetails> getDetailsByOrderId(Integer orderId) {
        return orderDetailsRepository.findByOrder_Id(orderId);
    }

    // ดู OrderDetails ตาม id
    public OrderDetails getOrderDetailById(Integer id) {
        return orderDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบรายละเอียดออเดอร์"));
    }

    // ลบรายการสินค้าออกจากออเดอร์
    public void deleteOrderDetail(Integer id) {
        OrderDetails detail = orderDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบรายละเอียดออเดอร์"));

        orderDetailsRepository.delete(detail);
    }
}