package com.example.smartOrder.dailyReport;

import com.example.smartOrder.order.Order;
import com.example.smartOrder.order.OrderRepository;
import com.example.smartOrder.orderdetails.OrderDetails;
import com.example.smartOrder.products.ProductRepository;
import com.example.smartOrder.products.Products;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DailyReportService {

    private final DailyReportRepository dailyReportRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public DailyReportService(
            DailyReportRepository dailyReportRepository,
            OrderRepository orderRepository,
            ProductRepository productRepository
    ) {
        this.dailyReportRepository = dailyReportRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public DailyReport generateReport(LocalDate date) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<Order> orders = orderRepository
                .findByCreatedAtGreaterThanEqualAndCreatedAtLessThan(start, end);

        BigDecimal totalSell = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;

        Map<String, Integer> productSalesMap = new HashMap<>();

        for (Order order : orders) {

            if (order.getOrderDetails() == null) {
                continue;
            }

            for (OrderDetails detail : order.getOrderDetails()) {

                int quantity = detail.getQuantity();

                BigDecimal detailTotalSell = detail.getTotalPrice() != null
                        ? detail.getTotalPrice()
                        : BigDecimal.ZERO;

                totalSell = totalSell.add(detailTotalSell);

                Products product = detail.getProduct();

                if (product != null) {

                    BigDecimal buyPrice = product.getBuyPrice() != null
                            ? product.getBuyPrice()
                            : BigDecimal.ZERO;

                    BigDecimal detailTotalCost =
                            buyPrice.multiply(BigDecimal.valueOf(quantity));

                    totalCost = totalCost.add(detailTotalCost);

                    String productName = product.getProductName();

                    productSalesMap.put(
                            productName,
                            productSalesMap.getOrDefault(productName, 0) + quantity
                    );
                }
            }
        }

        BigDecimal profit = totalSell.subtract(totalCost);

        String topSelling = productSalesMap.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey() + " (" + entry.getValue() + " ชิ้น)")
                .orElse("-");

        DailyReport report = dailyReportRepository.findByReportDate(date)
                .orElse(new DailyReport());

        report.setReportDate(date);
        report.setTotalSell(totalSell);
        report.setTotalCost(totalCost);
        report.setProfit(profit);
        report.setTopSelling(topSelling);
        report.setTotalOrders(orders.size());

        report.getOrders().clear();
        report.getOrders().addAll(orders);

        return dailyReportRepository.save(report);
    }

    public DailyReport getReportByDate(LocalDate date) {
        return dailyReportRepository.findByReportDate(date)
                .orElseThrow(() -> new RuntimeException("ไม่พบรายงานประจำวันที่ " + date));
    }

    public List<DailyReport> getAllReports() {
        return dailyReportRepository.findAll();
    }
}