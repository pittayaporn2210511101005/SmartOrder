package com.example.smartOrder.products;

import com.example.smartOrder.category.Category;
import com.example.smartOrder.category.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // เพิ่มสินค้า
    public Products createProduct(Products products) {

        String name = products.getProductName();

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("ชื่อสินค้าห้ามว่าง!");
        }

        name = name.trim();

        if (productRepository.existsByProductName(name)) {
            throw new IllegalArgumentException("ชื่อสินค้านี้มีอยู่แล้ว!");
        }

        products.setProductName(name);

        if (products.getCategory() == null || products.getCategory().getId() == null) {
            throw new IllegalArgumentException("กรุณาเลือกหมวดหมู่สินค้า!");
        }

        String categoryId = products.getCategory().getId();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        products.setCategory(category);

        return productRepository.save(products);
    }

    // ดึงสินค้าทั้งหมด
    public List<Products> getAllProducts() {
        return productRepository.findAll();
    }

    // ดึงสินค้าตาม id
    public Products getProductById(String id) {
        Long productId = Long.valueOf(id);

        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("ไม่พบID"));
    }

    // แก้ไขสินค้า
    public Products updateProduct(String id, Products product) {
        Long productId = Long.valueOf(id);

        Products existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("ไม่พบID"));

        String name = product.getProductName();

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("ชื่อสินค้าห้ามว่าง!");
        }

        existingProduct.setProductName(name.trim());
        existingProduct.setBuyPrice(product.getBuyPrice());
        existingProduct.setSellPrice(product.getSellPrice());
        existingProduct.setWarehouseStock(product.getWarehouseStock());
        existingProduct.setStoreStock(product.getStoreStock());
        existingProduct.setMinStockQty(product.getMinStockQty());

        // รูปภาพสินค้า
        // ถ้า frontend ส่ง null มา จะไม่ลบรูปเดิม
        // ถ้าต้องการลบรูป ให้ส่ง imageUrl เป็น "" มา
        if (product.getImageUrl() != null) {
            existingProduct.setImageUrl(product.getImageUrl());
        }

        // ถ้าส่ง category มา ค่อยเปลี่ยน category
        // ถ้าไม่ส่งมา ให้ใช้ category เดิม
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            String categoryId = product.getCategory().getId();

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            existingProduct.setCategory(category);
        }

        return productRepository.save(existingProduct);
    }

    // ลบสินค้า
    public void deleteProduct(String id) {
        Long productId = Long.valueOf(id);

        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(product);
    }

    // หาสินค้าแต่ละชนิดในประเภท
    public List<Products> getProductsByCategoryId(String categoryId) {
        return productRepository.findByCategory_Id(categoryId);
    }
}