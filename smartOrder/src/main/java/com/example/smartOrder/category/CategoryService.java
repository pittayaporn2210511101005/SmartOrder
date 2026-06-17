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

        if (category.getCategoryname() == null ||
                category.getCategoryname().trim().isEmpty()) {
            throw new RuntimeException("กรุณากรอกชื่อหมวดหมู่");
        }

        category.setId(null);
        category.setCategoryname(category.getCategoryname().trim());

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

    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบหมวดหมู่"));
    }

    public Category updateCategory(Integer id, Category category) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบหมวดหมู่"));

        if (category.getCategoryname() == null ||
                category.getCategoryname().trim().isEmpty()) {
            throw new RuntimeException("กรุณากรอกชื่อหมวดหมู่");
        }

        existingCategory.setCategoryname(category.getCategoryname().trim());

        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบหมวดหมู่"));

        long productCount = productRepository.countByCategory_Id(id);

        if (productCount > 0) {
            throw new RuntimeException("ไม่สามารถลบหมวดหมู่นี้ได้ เพราะมีสินค้าอยู่ในหมวดหมู่");
        }

        categoryRepository.delete(category);
    }
}