package com.example.smartOrder.products;

import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // เพิ่มสินค้า
    public Products createProduct(Products products) {
        if (productRepository.existsById(products.getId())) {
            throw new RuntimeException("รหัสสินค้านี้มีสินค้าอยู่แล้ว!");
        }
        LocalDateTime now = LocalDateTime.now();
        products.setCreateDate(now);
        products.setUpdateDate(now);

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
        existingProduct.setCostPrice(product.getCostPrice());
        existingProduct.setSellPrice(product.getSellPrice());
        existingProduct.setExpirydate(product.getExpirydate());
        existingProduct.setCategory(product.getCategory());

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