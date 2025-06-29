package com.bmcho.netflix.config;

import com.bmcho.netflix.security.NetflixUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final NetflixUserDetailsService netflixUserDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .userDetailsService(netflixUserDetailsService)
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers("/", "/register", "/api/v1/user/**", "/api/v1/auth/**").permitAll()
                                .anyRequest()
                                .authenticated())
                .oauth2Login(oauth2 -> oauth2.failureUrl("/login?error=true"))
                .build();
    }


    public CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();

            // 모든 헤더를 허용
            config.setAllowedHeaders(Collections.singletonList("*"));
            // 모든 HTTP 메서드를 허용 (GET, POST, PUT, DELETE 등)
            config.setAllowedMethods(Collections.singletonList("*"));
            // 모든 출처(origin)를 허용. 보안상 위험할 수 있음
            config.setAllowedOriginPatterns(Collections.singletonList("*"));
            // 인증 정보(쿠키, HTTP 인증)를 포함한 요청을 허용
            config.setAllowCredentials(true);
            return config;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
