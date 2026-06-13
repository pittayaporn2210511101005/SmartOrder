package com.example.smartOrder.products;

import com.example.smartOrder.category.Category;
import com.example.smartOrder.category.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;

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

    if (products.getCategory() != null) {
        String categoryId = products.getCategory().getId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        products.setCategory(category);
    }


        return productRepository.save(products);
    }

    // ดึงสินค้าทั้งหมด
    public List<Products> getAllProducts() {
        return productRepository.findAll();
    }

    // ดึงสินค้าตาม id
    public Products getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบID"));
    }

    // แก้ไขสินค้า
    public Products updateProduct(String id, Products product) {
        Products existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบID"));

        existingProduct.setProductName(product.getProductName());
        existingProduct.setSellPrice(product.getSellPrice());

        existingProduct.setWarehouseStock(
                product.getWarehouseStock());

        existingProduct.setStoreStock(
                product.getStoreStock());

        existingProduct.setMinStockQty(
                product.getMinStockQty());

        existingProduct.setCategory(
                product.getCategory());

        return productRepository.save(existingProduct);
    }

    // ลบสินค้า
    public void deleteProduct(String id) {
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(product);
    }

    //หาสินค้าแต่ละชนิดในประเภท
    public List<Products> getProductsByCategoryId(String categoryId) {
        return productRepository.findByCategory_Id(categoryId);
    }
}