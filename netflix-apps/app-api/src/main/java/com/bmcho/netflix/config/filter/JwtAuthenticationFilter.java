package com.bmcho.netflix.config.filter;

import com.bmcho.netflix.token.FetchTokenUseCase;
import com.bmcho.netflix.user.command.UserResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final FetchTokenUseCase fetchTokenUseCase;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain) throws ServletException, IOException {

        String accessToken = extractToken(request);

        if (accessToken != null && fetchTokenUseCase.validateToken((accessToken))) {
            Authentication authentication = getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    public Authentication getAuthentication(String accessToken) {
        UserResponse userByAccessToken = fetchTokenUseCase.findUserByAccessToken(accessToken);

        String role = Optional.ofNullable(userByAccessToken.role()).orElse("ROLE_ADMIN");
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

        String password = Optional.ofNullable(userByAccessToken.password()).orElse("password");
        UserDetails principal = new User(userByAccessToken.username(), password, authorities);
        return new UsernamePasswordAuthenticationToken(principal, userByAccessToken.userId(), authorities);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (!StringUtils.isBlank(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }

        return null;
    }

}
