package com.core.heartime.api.member.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    //이건 개발 다하고 나면 지울것
    // ▼▼▼ CHANGED: Swagger/정적 리소스/공개 경로는 필터 스킵
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/v3/api-docs")
                || uri.startsWith("/swagger-ui")
                || uri.equals("/swagger-ui.html")
                || uri.startsWith("/api/v3/api-docs")
                || uri.startsWith("/api/swagger-ui")
                || uri.startsWith("/swagger-resources")
                || uri.startsWith("/api/swagger-resources")
                || uri.startsWith("/api/webjars")
                || uri.startsWith("/css/") || uri.startsWith("/js/") || uri.startsWith("/images/")
                || "/".equals(uri) || "/error".equals(uri)
                || uri.startsWith("/api/v1/auth/") || uri.startsWith("/api/v1/posts/")
                || uri.startsWith("/api/v1/members/") || uri.startsWith("/api/v1/s3/")
                || uri.startsWith("/chat/inbox/") || uri.startsWith("/ws/");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            // 요청 헤더에서 토큰을 추출
            String token = resolveToken(request);

            // 토큰이 존재하고, 유효하다면
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 토큰에서 사용자 이메일 및 권한(role) 추출
                String email = jwtTokenProvider.getEmail(token);
                String role = jwtTokenProvider.getRole(token);

                // 권한 부여
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(email, null, List.of(authority));

                // SecurityContext에 인증 정보 저장
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            System.out.println("JWT Authentication Filter Failed: " + e.getMessage());
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("""
                                    {
                                        "status": 401,
                                        "error": "UNAUTHORIZED",
                                        "message": "인증되지 않은 사용자입니다."
                                    }
                                    """);
            return;
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    // Authorization 헤더에서 "Bearer {토큰}" 형식의 토큰을 파싱
    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // "Bearer " 이후의 실제 토큰만 추출
        }
        return null;
    }
}
