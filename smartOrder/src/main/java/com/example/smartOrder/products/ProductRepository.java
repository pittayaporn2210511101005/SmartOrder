package com.example.smartOrder.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {

    List<Products> findByCategory_Id(Integer categoryId);

    long countByCategory_Id(Integer categoryId);

    boolean existsByProductName(String productName);
}