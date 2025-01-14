package com.example.demo.config;

import com.example.demo.service.UserService;
import com.example.demo.util.TokenRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.Customizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserService userService;
    private final TokenRequestFilter tokenRequestFilter;
    /**
     * 비밀번호를 암호화하기 위한 PasswordEncoder 등록
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * DaoAuthenticationProvider를 통해 UserService(=UserDetailsService)와
     * PasswordEncoder를 연결
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * AuthenticationManager를 Bean으로 등록
     * (AuthenticationManagerBuilder를 HttpSecurity에서 공유 객체로 꺼내와
     *  authenticationProvider를 연결한 뒤 build)
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider())
                .build();
    }

    /**
     * Spring Security 6.1 이상에서는 WebSecurityConfigurerAdapter를 쓰지 않고,
     * SecurityFilterChain Bean을 통해 보안 설정을 구성
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 설정 - csrf()가 deprecated되었으므로 람다 형태 사용
                .csrf(CsrfConfigurer::disable)

                // 요청에 대한 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 로그인 요청을 허용해주지 않았다면 403이 발생
                        .requestMatchers("/user/login","/board/list").permitAll()
                        // 그 외 URL 별 권한 설정 ...
                        .anyRequest().permitAll()
                )

                // 세션을 사용하지 않도록 설정 (JWT 등 토큰 기반 인증 시)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 기본 로그인 폼 비활성화
                .formLogin(form -> form.disable())

                // CORS 설정
                .cors(Customizer.withDefaults());

        http.addFilterBefore(tokenRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // 토큰 필터 등을 추가해야 한다면 다음과 같이 가능
        // http.addFilterBefore(tokenRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
