package com.example.member.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "MEMBERS")
@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;
    
    public enum Role {
        USER, ADMIN
    }
}

