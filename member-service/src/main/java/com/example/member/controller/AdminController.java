package com.example.member.controller;

import com.example.member.model.Member;
import com.example.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
public class AdminController {

    private final MemberService memberService;

    public AdminController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public List<MemberResponse> getAllUsers() {
        return memberService.getAllMembers().stream()
                .map(member -> {
                    MemberResponse response = new MemberResponse();
                    response.setId(member.getId());
                    response.setUsername(member.getUsername());
                    response.setEmail(member.getEmail());
                    response.setRole(member.getRole().name());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            Member member = memberService.updateMember(
                    id,
                    request.get("username"),
                    request.get("email"),
                    request.get("role")
            );

            Map<String, Object> response = Map.of(
                    "message", "사용자 정보가 업데이트되었습니다.",
                    "id", member.getId(),
                    "username", member.getUsername(),
                    "email", member.getEmail(),
                    "role", member.getRole().name()
            );

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            memberService.deleteMember(id);
            return ResponseEntity.ok(Map.of("message", "사용자가 삭제되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 응답 DTO 클래스 (비밀번호 제외)
    public static class MemberResponse {
        private Long id;
        private String username;
        private String email;
        private String role;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}

