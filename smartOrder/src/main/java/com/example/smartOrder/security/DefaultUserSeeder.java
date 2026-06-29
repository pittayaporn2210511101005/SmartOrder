package com.example.smartOrder.security;

import com.example.smartOrder.admin.Admin;
import com.example.smartOrder.admin.AdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserSeeder implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default-admin.username:admin}")
    private String defaultAdminUsername;

    @Value("${app.default-admin.password:Admin@123456}")
    private String defaultAdminPassword;

    public DefaultUserSeeder(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!adminRepository.existsByUsername(defaultAdminUsername)) {
            Admin admin = new Admin();
            admin.setUsername(defaultAdminUsername);
            admin.setPassword(passwordEncoder.encode(defaultAdminPassword));
            admin.setRole(Role.ADMIN);

            adminRepository.save(admin);
            System.out.println("สร้าง admin เริ่มต้นเรียบร้อยแล้ว");
        }
    }
}