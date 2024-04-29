package com.study.securityoauth2.dto.response;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final OAuth2UserDto oAuth2UserDto;

    // 사용자 정보 반환
    // -> 소셜 프로바이더마다 데이터 형식이 달라 획일화 구현 불가
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    // 사용자 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return oAuth2UserDto.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return oAuth2UserDto.getName();
    }

    public String getCreatedUserId() {
        return oAuth2UserDto.getCreatedUserId();
    }
}
