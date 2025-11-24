package com.example.member.service;

import com.example.member.model.Member;
import com.example.member.repository.MemberRepository;
import com.example.member.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Member register(String username, String password, String email) {
        if (memberRepository.existsByUsername(username)) {
            throw new RuntimeException("이미 존재하는 사용자명입니다.");
        }
        if (memberRepository.existsByEmail(email)) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        Member member = new Member();
        member.setUsername(username);
        member.setPassword(passwordEncoder.encode(password));
        member.setEmail(email);
        member.setRole(Member.Role.USER);

        return memberRepository.save(member);
    }

    public String login(String username, String password) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자명 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new RuntimeException("사용자명 또는 비밀번호가 올바르지 않습니다.");
        }

        return jwtUtil.generateToken(member.getUsername(), member.getRole().name());
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public Member updateMember(Long id, String username, String email, String role) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        if (!member.getUsername().equals(username) && memberRepository.existsByUsername(username)) {
            throw new RuntimeException("이미 존재하는 사용자명입니다.");
        }
        if (!member.getEmail().equals(email) && memberRepository.existsByEmail(email)) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        member.setUsername(username);
        member.setEmail(email);
        if (role != null) {
            member.setRole(Member.Role.valueOf(role));
        }

        return memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}

