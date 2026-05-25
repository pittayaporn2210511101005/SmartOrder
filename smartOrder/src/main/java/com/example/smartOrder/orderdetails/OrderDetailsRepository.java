package com.example.smartOrder.orderdetails;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {

    List<OrderDetails> findByOrder_Id(Integer orderId);
}
