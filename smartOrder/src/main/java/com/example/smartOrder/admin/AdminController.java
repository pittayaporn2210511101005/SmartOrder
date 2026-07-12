package com.example.smartOrder.admin;

import com.example.smartOrder.category.Category;
import com.example.smartOrder.category.CategoryService;
import com.example.smartOrder.products.ProductService;
import com.example.smartOrder.products.Products;
import org.springframework.web.bind.annotation.*;
import com.example.smartOrder.order.MockOrderRequest;
import com.example.smartOrder.order.Order;
import com.example.smartOrder.order.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final AdminRepository adminRepository;
    private final OrderService orderService;

    public AdminController(
            CategoryService categoryService,
            ProductService productService,
            OrderService orderService,
            AdminRepository adminRepository
    ) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.orderService = orderService;
        this.adminRepository = adminRepository;
    }

    @PostMapping("/login")
    public String login(@RequestBody Admin request){
        Admin admin = adminRepository.findByUsername(request.getUsername());
        if(admin == null){
            return "ไม่พบผู้ใช้";
        }
        if(!admin.getPassword().equals(request.getPassword())){
            return "รหัสผ่านไม่ถูกต้อง";
        }
        return "success";
    }

    @PostMapping("/admins")
    public Admin createAdmin(@RequestBody Admin admin){
        return adminRepository.save(admin);
    }

    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody Products products) {
        try {
            Products savedProduct = productService.createProduct(products);
            return ResponseEntity.ok(savedProduct);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", e.getMessage()));

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }
    @GetMapping("/products")
    public List<Products> getAllProducts() {
        return productService.getAllProducts();
    }


    @PutMapping("/products/{id}")
    public Products updateProduct(@PathVariable String id, @RequestBody Products products) {
        return productService.updateProduct(id, products);
    }

    @DeleteMapping("/products/{id}")
    public String deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return "ลบสินค้าสำเร็จปิ้วๆ";
    }

    @PostMapping("/mock-orders")
    public ResponseEntity<?> createMockOrder(@RequestBody MockOrderRequest request) {
        Order order = orderService.createMockOrder(request);
        return ResponseEntity.ok(order);
    }


    //ประเภท
    @PostMapping("/categories")
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PutMapping("/categories/{id}")
    public Category updateCategory(@PathVariable Integer id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }
    // ลบประเภทสินค้า
    @DeleteMapping("/categories/{id}")
    public String deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return "ลบประเภทสินค้าสำเร็จ";
    }


}