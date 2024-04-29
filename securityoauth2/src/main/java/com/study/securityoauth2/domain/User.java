package com.study.securityoauth2.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User {

    // 유저 식별 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    // 소셜 ID
    @Column(name = "kakao_id")
    private String kakaoId;

    // 이름
    @Column(name = "name")
    private String name;

    // 이메일
    @Column(name = "email")
    private String email;

    // 권한
    @Column(name = "role")
    private String role;

    // 생성 날짜
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public User(String kakaoId, String name, String email, String role) {
        this.kakaoId = kakaoId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }
}
