package com.example.smartOrder.auth;

import com.example.smartOrder.admin.Admin;
import com.example.smartOrder.admin.AdminRepository;
import com.example.smartOrder.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Admin admin = adminRepository.findByUsername(request.getUsername())
                .orElse(null);

        if (admin == null) {
            return ResponseEntity.status(401).body(
                    Map.of("message", "ไม่พบผู้ใช้")
            );
        }

        boolean passwordMatch = passwordEncoder.matches(
                request.getPassword(),
                admin.getPassword()
        );

        if (!passwordMatch) {
            return ResponseEntity.status(401).body(
                    Map.of("message", "รหัสผ่านไม่ถูกต้อง")
            );
        }

        String token = jwtService.generateToken(admin);

        return ResponseEntity.ok(
                new LoginResponse(
                        token,
                        admin.getUsername(),
                        admin.getRole().name()
                )
        );
    }
}