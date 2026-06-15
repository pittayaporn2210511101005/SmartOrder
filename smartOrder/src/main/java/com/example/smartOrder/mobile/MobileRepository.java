package com.example.smartOrder.mobile;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MobileRepository
        extends JpaRepository<Mobile, String> {

    Mobile findByUsername(String username);
}