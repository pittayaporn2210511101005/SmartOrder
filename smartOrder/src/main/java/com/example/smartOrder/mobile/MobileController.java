package com.example.smartOrder.mobile;

import com.example.smartOrder.Noti.Notification;
import com.example.smartOrder.Noti.NotificationService;
import com.example.smartOrder.dailyReport.DailyReport;
import com.example.smartOrder.dailyReport.DailyReportService;
import com.example.smartOrder.order.Order;
import com.example.smartOrder.order.OrderService;
import com.example.smartOrder.orderdetails.OrderDetails;
import com.example.smartOrder.orderdetails.OrderDetailsRequest;
import com.example.smartOrder.orderdetails.OrderDetailsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mobile")
@CrossOrigin(origins = "http://localhost:5173")
public class MobileController {

    private final MobileRepository mobileRepository;
    private final OrderService orderService;
    private final OrderDetailsService orderDetailsService;
    private final DailyReportService dailyReportService;
    private final NotificationService notificationService;

    public MobileController(
            OrderService orderService,
            OrderDetailsService orderDetailsService,
            DailyReportService dailyReportService,
            MobileRepository mobileRepository,
            NotificationService notificationService
    ) {
        this.orderService = orderService;
        this.orderDetailsService = orderDetailsService;
        this.dailyReportService = dailyReportService;
        this.mobileRepository = mobileRepository;
        this.notificationService = notificationService;
    }

    @PostMapping("/login")
    public String login(@RequestBody Mobile request) {
        Mobile user = mobileRepository.findByUsername(request.getUsername());

        if (user == null) {
            return "ไม่พบผู้ใช้";
        }

        if (!user.getPassword().equals(request.getPassword())) {
            return "รหัสผ่านไม่ถูกต้อง";
        }

        return "success";
    }

    // มือถือสร้างออเดอร์ / สร้างบิลขาย
    @PostMapping("/orders")
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    // มือถือดูออเดอร์ทั้งหมด
    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // มือถือดูออเดอร์ตามไอดี
    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable Integer id) {
        return orderService.getOrderById(id);
    }

    // มือถือดูออเดอร์ตามวันที่
    // /api/mobile/orders/date?date=2026-05-22
    @GetMapping("/orders/date")
    public List<Order> getOrdersByDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return orderService.getOrdersByDate(date);
    }

    // แก้ไขยอดรวมออเดอร์
    @PutMapping("/orders/{id}")
    public Order updateOrder(@PathVariable Integer id, @RequestBody Order order) {
        return orderService.updateOrder(id, order);
    }

    // ลบออเดอร์
    @DeleteMapping("/orders/{id}")
    public String deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return "ลบออเดอร์สำเร็จ";
    }

    // เพิ่มสินค้าเข้าออเดอร์
    @PostMapping("/orders/{orderId}/details")
    public ResponseEntity<?> createOrderDetail(
            @PathVariable Integer orderId,
            @RequestBody OrderDetailsRequest request
    ) {
        try {
            OrderDetails detail = orderDetailsService.createOrderDetail(
                    orderId,
                    request.getProductId(),
                    request.getQuantity(),
                    request.getStockType()
            );

            return ResponseEntity.ok(detail);

        } catch (RuntimeException e) {
            orderService.markOrderFailed(orderId, e.getMessage());

            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // ดูรายการสินค้าใน order
    @GetMapping("/orders/{orderId}/details")
    public List<OrderDetails> getDetailsByOrderId(@PathVariable Integer orderId) {
        return orderDetailsService.getDetailsByOrderId(orderId);
    }

    // dailyReport
    @GetMapping("/daily-reports")
    public List<DailyReport> getAllReports() {
        return dailyReportService.getAllReports();
    }

    @GetMapping("/daily-reports/today")
    public DailyReport getTodayReport() {
        return dailyReportService.getReportByDate(LocalDate.now());
    }

    @PostMapping("/daily-reports/generate-today")
    public DailyReport generateTodayReport() {
        return dailyReportService.generateReport(LocalDate.now());
    }

    @GetMapping("/notifications")
    public List<Notification> getMobileNotifications() {
        return notificationService.getMobileNotifications();
    }

    @GetMapping("/notifications/unread")
    public List<Notification> getUnreadMobileNotifications() {
        return notificationService.getUnreadMobileNotifications();
    }

    @PutMapping("/notifications/{id}/read")
    public Notification markNotificationAsRead(@PathVariable Integer id) {
        return notificationService.markAsRead(id);
    }
}