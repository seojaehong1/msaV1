package com.example.member.config;

import com.example.member.model.Member;
import com.example.member.repository.MemberRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        if (memberRepository.count() == 0) {
            // 관리자 계정
            Member admin = new Member();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setRole(Member.Role.ADMIN);
            memberRepository.save(admin);

            // 일반 사용자 계정 1
            Member user1 = new Member();
            user1.setUsername("user1");
            user1.setPassword(passwordEncoder.encode("user123"));
            user1.setEmail("user1@example.com");
            user1.setRole(Member.Role.USER);
            memberRepository.save(user1);

            // 일반 사용자 계정 2
            Member user2 = new Member();
            user2.setUsername("user2");
            user2.setPassword(passwordEncoder.encode("user123"));
            user2.setEmail("user2@example.com");
            user2.setRole(Member.Role.USER);
            memberRepository.save(user2);
        }
    }
}

