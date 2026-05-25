package com.example.smartOrder.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, String> {
    List<Products> findByCategory_Id(String categoryId);

}
