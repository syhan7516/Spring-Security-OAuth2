package com.study.securityoauth2.oauth2;

import com.study.securityoauth2.dto.response.CustomOAuth2User;
import com.study.securityoauth2.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("로그인 성공 : OK");

        // 유저 정보 가져오기
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        // 유저 ID 정보
        String createdUserId = customUserDetails.getCreatedUserId();

        // 유저 권한 정보
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 토큰 발행 (1시간, 24시간)
        String accessToken = jwtUtil.createJwt(createdUserId, role, 1000*60*60L);
        String refreshToken = jwtUtil.createJwt(createdUserId, role, 1000*60*60*24L);

        // 액세스 토큰 헤더에 추가
        response.addHeader("Authorization",accessToken);
        
        // 리프레쉬 토큰 쿠키에 추가
        response.addCookie(createCookie("Authorization", refreshToken));
        response.sendRedirect("${spring.server.url}");

        log.info("로그인 성공 : COMPLETE");
    }

    // 쿠키 생성 메서드
    private Cookie createCookie(String key, String value) {
        
        // 쿠키 객체 생성
        Cookie cookie = new Cookie(key, value);
        
        // 쿠키 만료 시간 설정
        cookie.setMaxAge(1000*60*60*24);
        
        // https 환경에서만 사용가능하도록 설정
        //cookie.setSecure(true);
        
        // 쿠키가 보일 위치 설정
        cookie.setPath("/");

        // 브라우저에서 JS 접근 불가하도록 설정
        cookie.setHttpOnly(true);

        return cookie;
    }
}
