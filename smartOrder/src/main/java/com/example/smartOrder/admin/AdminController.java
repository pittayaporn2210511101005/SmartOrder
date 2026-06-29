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


    @PostMapping("/admins")
    public Admin createAdmin(@RequestBody Admin admin){
        return adminRepository.save(admin);
    }

    @PostMapping("/products")
    public Products createProduct(@RequestBody Products products) {
        return productService.createProduct(products);
    }
    @GetMapping("/products")
    public List<Products> getAllProducts() {
        return productService.getAllProducts();
    }
    @GetMapping("/products/{id}")
    public Products getProductById(@PathVariable String id) {
        return productService.getProductById(id);
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
    // เพิ่มประเภทสินค้า
    @PostMapping("/categories")
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }
    // ดูประเภทสินค้าทั้งหมด
    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }
    // ดูประเภทสินค้าตาม id
    @GetMapping("/categories/{id}/products")
    public List<Products> getProductsByCategoryId(@PathVariable Integer id) {
        return productService.getProductsByCategoryId(id);
    }
    // แก้ไขประเภทสินค้า
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