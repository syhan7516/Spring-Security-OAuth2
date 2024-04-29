package com.study.securityoauth2.service;

import com.study.securityoauth2.domain.User;
import com.study.securityoauth2.dto.response.CustomOAuth2User;
import com.study.securityoauth2.dto.response.KakaoResponse;
import com.study.securityoauth2.dto.response.OAuth2Response;
import com.study.securityoauth2.dto.response.OAuth2UserDto;
import com.study.securityoauth2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


// DefaultOAuth2UserService 가지는 기본 메서드들은 Client 정보를 획득하는 것이 목적인 클래스

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

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

        // 리소스 서버에서 발급받은 정보로 사용자 아이디 값 생성
        String createdUserId = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();

        // 생성된 값 이미 존재하는지 확인
        User existData = userRepository.findByKakaoId(createdUserId);

        // 존재하지 않는 경우
        if(existData == null) {

            // 유저 생성
            User user = User.builder()
                    .kakaoId(createdUserId)
                    .name(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .role("ROLE_USER")
                    .build();

            userRepository.save(user);

            // OAuth2User 인자 객체 생성
            OAuth2UserDto oAuth2UserDto = OAuth2UserDto.builder()
                    .role("ROLE_USER")
                    .name(oAuth2Response.getName())
                    .createdUserId(createdUserId)
                    .build();

            // Authentication Provider 전달 객체 생성 후 반환
            return new CustomOAuth2User(oAuth2UserDto);
        }
        
        // 이미 존재하는 경우
        else {

            // OAuth2User 인자 객체 생성
            OAuth2UserDto oAuth2UserDto = OAuth2UserDto.builder()
                    .role(existData.getRole())
                    .name(oAuth2User.getName())
                    .createdUserId(createdUserId)
                    .build();

            // Authentication Provider 전달 객체 생성 후 반환
            return new CustomOAuth2User(oAuth2UserDto);
        }
    }
}
