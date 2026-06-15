package com.example.smartOrder.category;

import com.example.smartOrder.products.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(
            CategoryRepository categoryRepository,
            ProductRepository productRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public Category createCategory(Category category) {
        if (category.getId() == null || category.getId().trim().isEmpty()) {
            throw new RuntimeException("กรุณากรอกรหัสหมวดหมู่");
        }

        if (category.getCategoryname() == null || category.getCategoryname().trim().isEmpty()) {
            throw new RuntimeException("กรุณากรอกชื่อหมวดหมู่");
        }

        String id = category.getId().trim();
        String name = category.getCategoryname().trim();

        if (categoryRepository.existsById(id)) {
            throw new RuntimeException("รหัสหมวดหมู่ซ้ำ");
        }

        category.setId(id);
        category.setCategoryname(name);

        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {
            long count = productRepository.countByCategory_Id(category.getId());
            category.setProductCount((int) count);
        }

        return categories;
    }

    public Category getCategoryById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบหมวดหมู่"));
    }

    public Category updateCategory(String id, Category category) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบหมวดหมู่"));

        if (category.getCategoryname() == null || category.getCategoryname().trim().isEmpty()) {
            throw new RuntimeException("กรุณากรอกชื่อหมวดหมู่");
        }

        existingCategory.setCategoryname(category.getCategoryname().trim());

        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบหมวดหมู่"));

        long productCount = productRepository.countByCategory_Id(id);

        if (productCount > 0) {
            throw new RuntimeException("ไม่สามารถลบหมวดหมู่นี้ได้ เพราะมีสินค้าอยู่ในหมวดหมู่");
        }

        categoryRepository.delete(category);
    }
}