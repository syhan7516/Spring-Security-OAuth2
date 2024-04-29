package com.study.securityoauth2.repository;

import com.study.securityoauth2.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // 카카오 아이디로 유저 찾기
    User findByKakaoId(String kakaoId);
}
