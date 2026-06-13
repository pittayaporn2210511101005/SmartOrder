package com.example.smartOrder.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, String> {

    List<Products> findByCategory_Id(String categoryId);
    boolean existsByProductName(String productName);
    @Query("""
SELECT COALESCE(
SUM(p.warehouseStock + p.storeStock),
0
)
FROM Products p
""")
    Long sumAllStock();
}