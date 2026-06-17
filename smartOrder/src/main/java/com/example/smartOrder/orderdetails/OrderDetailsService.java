package com.example.smartOrder.orderdetails;

import com.example.smartOrder.order.Order;
import com.example.smartOrder.order.OrderRepository;
import com.example.smartOrder.products.ProductRepository;
import com.example.smartOrder.products.Products;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderDetailsService(
            OrderDetailsRepository orderDetailsRepository,
            OrderRepository orderRepository,
            ProductRepository productRepository
    ) {
        this.orderDetailsRepository = orderDetailsRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    // เพิ่มรายการสินค้าเข้าออเดอร์
    @Transactional
    public OrderDetails createOrderDetail(
            Integer orderId,
            String productId,
            int quantity,
            String stockType
    ) {
        if (quantity <= 0) {
            throw new RuntimeException("จำนวนสินค้าต้องมากกว่า 0");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("ไม่พบออเดอร์"));

        Long productIdLong = Long.valueOf(productId);

        Products product = productRepository.findById(productIdLong)
                .orElseThrow(() -> new RuntimeException("ไม่พบสินค้า"));

        if (stockType == null || stockType.isBlank()) {
            stockType = "store";
        }

        int warehouseStock = product.getWarehouseStock();
        int storeStock = product.getStoreStock();

        if ("warehouse".equals(stockType)) {
            // เลือกโกดัง = ตัดเฉพาะสต๊อกโกดังเท่านั้น
            if (warehouseStock < quantity) {
                throw new RuntimeException(
                        "เลือกขายจากโกดัง: สินค้า " + product.getProductName() +
                                " ในโกดังไม่เพียงพอ ต้องการ " + quantity +
                                " ชิ้น แต่โกดังมี " + warehouseStock + " ชิ้น"
                );
            }

            product.setWarehouseStock(warehouseStock - quantity);

        } else if ("store".equals(stockType)) {
            // เลือกหน้าร้าน = ตัดหน้าร้านก่อน ถ้าไม่พอค่อยดึงโกดัง
            int totalAvailable = storeStock + warehouseStock;

            if (totalAvailable < quantity) {
                throw new RuntimeException(
                        "เลือกขายจากหน้าร้าน: สินค้า " + product.getProductName() +
                                " ไม่เพียงพอ ต้องการ " + quantity +
                                " ชิ้น หน้าร้านมี " + storeStock +
                                " ชิ้น โกดังมี " + warehouseStock +
                                " ชิ้น รวมมี " + totalAvailable + " ชิ้น"
                );
            }

            if (storeStock >= quantity) {
                product.setStoreStock(storeStock - quantity);
            } else {
                int needFromWarehouse = quantity - storeStock;

                product.setStoreStock(0);
                product.setWarehouseStock(warehouseStock - needFromWarehouse);
            }

        } else {
            throw new RuntimeException("ประเภทสต๊อกไม่ถูกต้อง");
        }

        BigDecimal sellingPrice = product.getSellPrice();
        BigDecimal totalPrice = sellingPrice.multiply(BigDecimal.valueOf(quantity));

        OrderDetails detail = new OrderDetails();
        detail.setOrder(order);
        detail.setProduct(product);
        detail.setQuantity(quantity);
        detail.setSellingPrice(sellingPrice);
        detail.setTotalPrice(totalPrice);

        productRepository.save(product);

        OrderDetails savedDetail = orderDetailsRepository.save(detail);

        int newTotalSell = order.getTotalSell() + totalPrice.intValue();

        order.setTotalSell(newTotalSell);
        order.setStatus("SUCCESS");
        order.setFailReason(null);

        orderRepository.save(order);

        return savedDetail;
    }

    // ดูรายการสินค้าในออเดอร์
    public List<OrderDetails> getDetailsByOrderId(Integer orderId) {
        return orderDetailsRepository.findByOrder_Id(orderId);
    }

    // ดูรายละเอียดตาม id
    public OrderDetails getOrderDetailById(Integer id) {
        return orderDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบรายละเอียดออเดอร์"));
    }

    // ลบรายการสินค้า
    public void deleteOrderDetail(Integer id) {
        OrderDetails detail = orderDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบรายละเอียดออเดอร์"));

        orderDetailsRepository.delete(detail);
    }
}