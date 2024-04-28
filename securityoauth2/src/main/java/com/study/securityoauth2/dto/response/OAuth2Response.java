package com.study.securityoauth2.dto.response;

// 소셜 로그인 응답 인터페이스
public interface OAuth2Response {

    // 등록 서비스 제공자
    String getProvider();
    
    // 아이디
    String getProviderId();

    // 이메일
    String getEmail();

    // 사용자 이름
    String getName();

}
