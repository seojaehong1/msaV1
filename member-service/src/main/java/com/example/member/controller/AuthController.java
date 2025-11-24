package com.example.member.controller;

import com.example.member.model.Member;
import com.example.member.service.MemberService;
import com.example.member.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public AuthController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            Member member = memberService.register(
                    request.getUsername(),
                    request.getPassword(),
                    request.getEmail()
            );

            Map<String, String> response = new HashMap<>();
            response.put("message", "회원 가입이 완료되었습니다.");
            response.put("username", member.getUsername());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = memberService.login(request.getUsername(), request.getPassword());
            Member member = memberService.getAllMembers().stream()
                    .filter(m -> m.getUsername().equals(request.getUsername()))
                    .findFirst()
                    .orElseThrow();

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("username", member.getUsername());
            response.put("role", member.getRole().name());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DTO 클래스
    public static class RegisterRequest {
        private String username;
        private String password;
        private String email;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}

