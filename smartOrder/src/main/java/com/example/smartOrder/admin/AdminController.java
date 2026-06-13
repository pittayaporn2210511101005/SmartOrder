package com.example.smartOrder.admin;

import com.example.smartOrder.category.Category;
import com.example.smartOrder.category.CategoryService;
import com.example.smartOrder.products.ProductService;
import com.example.smartOrder.products.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    private final ProductService productService;
    private final CategoryService categoryService;


    public AdminController(
            ProductService productService,
            CategoryService categoryService){
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @Autowired
    private AdminRepository adminRepository;

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
    //สินค้า
    // เพิ่มสินค้า
    @PostMapping("/products")
    public Products createProduct(@RequestBody Products products) {
        return productService.createProduct(products);
    }
    // ดึงสินค้าทั้งหมด
    @GetMapping("/products")
    public List<Products> getAllProducts() {
        return productService.getAllProducts();
    }
    // ดึงสินค้าตาม id
    @GetMapping("/products/{id}")
    public Products getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }
    // แก้ไขสินค้า
    @PutMapping("/products/{id}")
    public Products updateProduct(@PathVariable String id, @RequestBody Products products) {
        return productService.updateProduct(id, products);
    }
    // ลบสินค้า
    @DeleteMapping("/products/{id}")
    public String deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return "ลบสินค้าสำเร็จปิ้วๆ";
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
    public List<Products> getProductsByCategoryId(@PathVariable String id) {
        return productService.getProductsByCategoryId(id);
    }
    // แก้ไขประเภทสินค้า
    @PutMapping("/categories/{id}")
    public Category updateCategory(@PathVariable String id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }
    // ลบประเภทสินค้า
    @DeleteMapping("/categories/{id}")
    public String deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return "ลบประเภทสินค้าสำเร็จ";
    }


}