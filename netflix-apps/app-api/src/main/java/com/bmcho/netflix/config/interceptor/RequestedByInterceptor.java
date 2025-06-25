package com.bmcho.netflix.config.interceptor;

import com.bmcho.netflix.authentication.AuthenticationHolder;
import com.bmcho.netflix.authentication.RequestedBy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

@Component
@RequiredArgsConstructor
public class RequestedByInterceptor implements WebRequestInterceptor {
    public static final String REQUESTED_BY_HEADER = "requested-by";

    private final AuthenticationHolder authenticationHolder;

    @Override
    public void preHandle(WebRequest request) throws Exception {
        String requestedBy = request.getHeader(REQUESTED_BY_HEADER);
        RequestedBy requestUser = new RequestedBy(requestedBy);
        authenticationHolder.setAuthentication(requestUser);
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {

    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {

    }
}

/*
    위 authentication는 잘 짜여진 코드이지만 과도한 Interface화는 코드의 많은 복잡성을 불러일으킴
    밑 처럼 class 선언을 통해 작업을 하더라도 크게 문제는 없다.

    장점 : 테스트 간단해짐
    단점 : 추후 확장을 위해선 상황에 따라 추상화가 필수
    
    추후 spring security 를 통한 인증 체계를 완성할 것이기 때문에 추상화가 필수가 될수도 있다.

@Getter
@AllArgsConstructor
public class Authentication {
    private final String requestedBy;
}

@Component
public class AuthenticationHolder {
    private Authentication authentication;

    public Optional<Authentication> getAuthentication() {
        return Optional.ofNullable(authentication);
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    // 요청자 ID 바로 조회용 메서드 추가 (선택)
    public Optional<String> getRequestedBy() {
        return getAuthentication().map(Authentication::getRequestedBy);
    }
}

@Component
public class RequestedByInterceptor implements WebRequestInterceptor {
    private static final String REQUESTED_BY_HEADER = "requested-by";
    private final AuthenticationHolder authenticationHolder;

    public RequestedByInterceptor(AuthenticationHolder authenticationHolder) {
        this.authenticationHolder = authenticationHolder;
    }

    @Override
    public void preHandle(WebRequest request) {
        String requestedBy = request.getHeader(REQUESTED_BY_HEADER);
        if (requestedBy != null) {
            authenticationHolder.setAuthentication(new Authentication(requestedBy));
        }
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) {}

    @Override
    public void afterCompletion(WebRequest request, Exception ex) {}
}

*/