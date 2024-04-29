package com.study.securityoauth2.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuth2UserDto {

    // 사용자 권한
    private String role;

    // 사용자 이름
    private String name;

    // 생성된 사용자 아아디
    private String createdUserId;

    @Builder
    public OAuth2UserDto(String role, String name, String createdUserId) {
        this.role = role;
        this.name = name;
        this.createdUserId = createdUserId;
    }
}
