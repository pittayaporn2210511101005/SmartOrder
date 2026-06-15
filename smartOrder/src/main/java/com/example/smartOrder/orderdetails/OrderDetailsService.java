package com.example.smartOrder.orderdetails;

import com.example.smartOrder.order.Order;
import com.example.smartOrder.order.OrderRepository;
import com.example.smartOrder.products.ProductRepository;
import com.example.smartOrder.products.Products;
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

        if (stockType.equals("warehouse")) {
            if (warehouseStock < quantity) {
                throw new RuntimeException(
                        "สินค้า " + product.getProductName() +
                                " ในโกดังไม่เพียงพอ"
                );
            }

            product.setWarehouseStock(warehouseStock - quantity);

        } else {
            int totalAvailable = storeStock + warehouseStock;

            if (totalAvailable < quantity) {
                throw new RuntimeException(
                        "สินค้า " + product.getProductName() +
                                " ไม่เพียงพอ หน้าร้านมี " + storeStock +
                                " ชิ้น โกดังมี " + warehouseStock + " ชิ้น"
                );
            }

            if (storeStock >= quantity) {
                product.setStoreStock(storeStock - quantity);
            } else {
                int needFromWarehouse = quantity - storeStock;

                product.setStoreStock(0);
                product.setWarehouseStock(warehouseStock - needFromWarehouse);
            }
        }

        productRepository.save(product);

        BigDecimal sellingPrice = product.getSellPrice();
        BigDecimal totalPrice =
                sellingPrice.multiply(BigDecimal.valueOf(quantity));

        OrderDetails detail = new OrderDetails();
        detail.setOrder(order);
        detail.setProduct(product);
        detail.setQuantity(quantity);
        detail.setSellingPrice(sellingPrice);
        detail.setTotalPrice(totalPrice);

        OrderDetails savedDetail = orderDetailsRepository.save(detail);

        order.setTotalSell(order.getTotalSell() + totalPrice.intValue());
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