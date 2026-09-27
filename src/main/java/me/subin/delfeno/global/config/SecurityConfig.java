package me.subin.delfeno.global.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * @author 박 수 빈
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] ALLOWED_URL_PATTERNS = {
            "/",
            "/sign-up",
            "/api/v1/auth/**",
            "/api/v1/users/is-duplicated-email/**",
            "/api/v1/users/is-duplicated-nickname/**",
            "/h2-console/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer customizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public HttpSessionEventPublisher sessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /* HTTP 기본 인증 비활성화 */
        http.httpBasic(AbstractHttpConfigurer::disable);
        /* 폼 로그인 비활성화 */
        http.formLogin(AbstractHttpConfigurer::disable);
        /* CSRF 공격 방어 비활성화 */
        http.csrf(AbstractHttpConfigurer::disable);

        /* X-FRAME-OPTIONS 헤더 설정 : H2 콘솔 화면 깨짐 방지를 위한 동일 도메인 내 iframe 접근 허용 */
        http.headers(header -> header
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        /* 세션 관리 정책 설정 */
        http.sessionManagement(session -> session
                // 사용자 당 허용될 최대 세션 수
                .maximumSessions(1)
                // true: 새로운 로그인 차단 || false(default): 기존 세션 만료
                .maxSessionsPreventsLogin(false)
                // 세션 만료시 이동할 URL
                .expiredUrl("/"));

        /* HTTP 요청 인가 정책 설정 */
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(ALLOWED_URL_PATTERNS).permitAll()
                .anyRequest().authenticated());

        return http.build();
    }
}