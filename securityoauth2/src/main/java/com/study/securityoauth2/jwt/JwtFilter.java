package com.study.securityoauth2.jwt;

import com.study.securityoauth2.dto.response.CustomOAuth2User;
import com.study.securityoauth2.dto.response.OAuth2UserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 쿠키 불러오기
        String authorization = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            // 쿠키 키가 Authorization 경우
            if (cookie.getName().equals("Authorization")) {

                // 해당 쿠키 값 얻기
                authorization = cookie.getValue();
            }
        }

        // Authorization 헤더 검증

        // Authorization 없는 경우
        if (authorization == null) {

            System.out.println("토큰이 없습니다.");
            filterChain.doFilter(request, response);

            // 조건이 해당되면 메소드 종료 (필수)
            return;
        }

        // 토큰 만료 시간 검증
        if (jwtUtil.isExpired(authorization)) {

            System.out.println("토큰이 만료되었습니다.");
            filterChain.doFilter(request, response);

            // 조건이 해당되면 메소드 종료 (필수)
            return;
        }

        // 토큰에서 createdId, role 확인
        String createdId = jwtUtil.getCreatedId(authorization);
        String role = jwtUtil.getRole(authorization);

        // OAuth2UserDto 생성
        OAuth2UserDto oAuth2UserDto = OAuth2UserDto.builder()
                .createdUserId(createdId)
                .role(role)
                .build();

        // UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(oAuth2UserDto);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}