package com.study.securityoauth2.dto.response;

import lombok.RequiredArgsConstructor;

import java.util.Map;

// 소셜 로그인 카카오 응답 클래스 구현
@RequiredArgsConstructor
public class KakaoResponse implements OAuth2Response {

    // 로그인 후 받은 유저 정보
    private final Map<String, Object> attribute;

    // 소셜 종류 확인
    @Override
    public String getProvider() {
        return "kakao";
    }
    
    // 소셜 ID 확인
    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    // 소셜 로그인 이메일
    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    // 이름
    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}
