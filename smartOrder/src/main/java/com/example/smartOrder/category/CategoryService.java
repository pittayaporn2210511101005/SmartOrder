package com.example.smartOrder.category;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // เพิ่มประเภทสินค้า
    public Category createCategory(Category category) {
        if (categoryRepository.existsById(category.getId())) {
            throw new RuntimeException("!รหัสสินค้าของคุณซ้ำ!");
        }
        return categoryRepository.save(category);
    }

    // ดึงประเภทสินค้าทั้งหมด
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // ดึงประเภทสินค้าตาม id
    public Category getCategoryById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("คุณใส่เลขIDประเภทสินค้าผิด"));
    }

    // แก้ไขประเภทสินค้า
    public Category updateCategory(String id, Category category) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("คุณใส่เลขIDประเภทสินค้าผิด"));

        existingCategory.setCategoryname(category.getCategoryname());

        return categoryRepository.save(existingCategory);
    }

    // ลบประเภทสินค้า
    public void deleteCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        categoryRepository.delete(category);
    }
}