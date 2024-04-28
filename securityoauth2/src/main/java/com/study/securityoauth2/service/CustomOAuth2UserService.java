package com.study.securityoauth2.service;

import com.study.securityoauth2.dto.response.KakaoResponse;
import com.study.securityoauth2.dto.response.OAuth2Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


// DefaultOAuth2UserService 가지는 기본 메서드들은 Client 정보를 획득하는 것이 목적인 클래스

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    // OAuth2UserRequest 리소스 서버에서 제공되는 Client 정보
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // DefaultOAuth2UserService 가지는 생성자를 불러 loadUser에 userRequest 전달하여 값을 획득
        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("oauth2 user : {}", oAuth2User.getAttributes().toString());

        // 등록 서비스 조회
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 소셜 로그인 응답 인터페이스 생성
        OAuth2Response oAuth2Response = null;

        // 등록 서비스가 카카오인 경우
        if(registrationId.equals("kakao")) {

            log.info("kakao : OK");
            
            // 소셜 로그인 응답 카카오 구현체
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }

        // 이외 경우
        else {

            return null;
        }

        // 로그인 완료 후 추가 작업

        return oAuth2User;
    }
}
