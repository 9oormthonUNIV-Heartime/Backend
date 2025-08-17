package com.core.heartime.common.config;

import com.core.heartime.api.members.jwt.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // 설정 클래스로 인식
@EnableWebSecurity // Spring Security 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    // JWT 인증 필터 의존성 주입
    private final JwtAuthenticationFilter jwtFilter;

    // 비밀번호 암호화를 위한 Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt 방식 사용
    }

    // Spring Security 설정 메인 메서드
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 메서드 내부 화이트리스트(지역 변수)
        String[] swagger = {
                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                "/api/v3/api-docs/**", "/api/swagger-ui/**", "/api/swagger-ui.html",
                "/swagger-resources/**", "/api/swagger-resources/**", "/api/webjars/**"
        };

        String[] publics = {
                "/", "/login", "/signup",
                "/css/**", "/js/**", "/images/**", "/favicon.ico", "/error",
                "/chat/inbox/**", "/ws/**",
                // 개발 단계에서 임시 오픈
                "/api/v1/auth/**", "/api/v1/posts/**", "/api/v1/members/**", "/api/v1/s3/**", "/api/v1/health/**"
        };


        return http
                .httpBasic(AbstractHttpConfigurer::disable)

                // ▼ 추가: 로그인폼/로그아웃 리다이렉트 방지
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                // ▲

                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .sessionManagement(config -> config
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(swagger).permitAll() // Swagger 전부 허용
                        .requestMatchers(publics).permitAll() // 정적/공개 API 허용
                        .anyRequest().authenticated()         // 나머지는 인증
                )
                // 인증 필요 URL 접근 시 401 JSON(리다이렉트 X)
                .exceptionHandling(e -> e.authenticationEntryPoint((req, res, ex) -> {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.setContentType("application/json;charset=UTF-8");
                    res.getWriter().write("{\"status\":401,\"error\":\"UNAUTHORIZED\",\"message\":\"인증이 필요합니다.\"}");
                }))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build(); // SecurityFilterChain 반환
    }
}
