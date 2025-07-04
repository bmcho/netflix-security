package com.bmcho.netflix.controller.user;

import com.bmcho.netflix.controller.NetflixApiResponse;
import com.bmcho.netflix.controller.user.reqeust.UserLoginRequest;
import com.bmcho.netflix.controller.user.reqeust.UserRegistrationRequest;
import com.bmcho.netflix.security.NetflixAuthUser;
import com.bmcho.netflix.token.FetchTokenUseCase;
import com.bmcho.netflix.token.UpdateTokenUseCase;
import com.bmcho.netflix.user.FetchUserUseCase;
import com.bmcho.netflix.user.RegisterUserUseCase;
import com.bmcho.netflix.user.command.UserRegistrationCommand;
import com.bmcho.netflix.user.command.UserResponse;
import com.bmcho.netflix.user.response.UserRegistrationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationManager authenticationManager;

    private final RegisterUserUseCase registerUserUseCase;
    private final FetchTokenUseCase fetchTokenUseCase;
    private final FetchUserUseCase fetchUserUseCase;
    private final UpdateTokenUseCase updateTokenUseCase;

    @PostMapping("/user/register")
    public NetflixApiResponse<UserRegistrationResponse> userRegister(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        UserRegistrationResponse userRegistrationResponse = registerUserUseCase.register(
            UserRegistrationCommand.builder()
                .email(userRegistrationRequest.getEmail())
                .encryptedPassword(userRegistrationRequest.getPassword())
                .phone(userRegistrationRequest.getPhone())
                .username(userRegistrationRequest.getUsername())
                .build()
        );

        return NetflixApiResponse.ok(userRegistrationResponse);
    }

    @PostMapping("/user/login")
    public NetflixApiResponse<String> login(@RequestBody UserLoginRequest userLoginRequest) {
        String email = userLoginRequest.getEmail();
        String password = userLoginRequest.getPassword();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);

        /**
         * 1. AuthenticationManagerBuilder에서 구성된 AuthenticationManager 인스턴스를 가져온다.  --> AuthenticationManager 직접 빈등록해서 사용
         * 2. AuthenticationManager의 authenticate 메서드를 호출하여 실제 인증을 수행하게 되는데 현재 프로젝트 기준으로
         *    UserDetailsService를 구현한 NetflixUserDetailsService의 loadUserByUsername 메서드를 통하여 주어진 email에 대한 User를 조회한다.
         * 3. PasswordEncoder를 통해 입력받은 Password와 DB에서 조회한 User의 Password를 검증한다.
         * 4. 여기서 일치하지 않으면 (org.springframework.security.authentication.BadCredentialsException: 자격 증명에 실패하였습니다.)
         *    라는 exception이 발생한다.
         * 5. 입력받은 email에 대한 User가 없다면 loadUserByUsername 메서드 단계에서 예외를 던져 준다.
         *
         * ##  Deep  ##
         *     ProviderManger 는 AuthenticationManager 의 구현체이며 모든 Provider 를 관리
         *     우리는 email(username), password 자격 증명이기 때문에 AbstractUserDetailsAuthenticationProvider 를 상속받는 DaoAuthenticationProvider 를 사용하게 됨
         *     Builder 통해 userDetailService 를 등록하면 DaoAuthenticationConfigurer 통해 등록된 userDetailService 가지고 있는 DaoAuthenticationProvider 를 생성
         */

        Authentication authentication = authenticationManager.authenticate(token);
        NetflixAuthUser netflixAuthUser = (NetflixAuthUser) authentication.getPrincipal();
        String accessToken = updateTokenUseCase.updateInsertToken(netflixAuthUser.getUserId());

        return NetflixApiResponse.ok(accessToken);
    }

    // front-end 에서 리다이렉트를 받고 처리 하는 과정
    @PostMapping("/user/callback")
    public NetflixApiResponse<String> kakaoLoginCallback(@RequestParam String code, @RequestParam String provider) {

        // 받은 코드를 KaKao 인증서버에 전송 및 유효성 검사 이후 토큰 리턴
        String socialAccessToken = fetchTokenUseCase.getTokenByCode(provider, code);
        // 사용자 정보 요청
        UserResponse user = fetchUserUseCase.findUserAccessToken(provider, socialAccessToken);
        // 소셜 유저 확인 및 저장
        UserResponse userByProviderId = fetchUserUseCase.findByProviderId(user.providerId());
        if (userByProviderId == null) {
            // 저장
            registerUserUseCase.registerSocialUser(
                user.username(),
                user.provider(),
                user.providerId()
            );
        }

        /* TODO: 2025-07-1, 화, 11:57 bmcho12
         *  추후 비대칭키 변경
         */
        //서버내 secret key 로 server token 생성
        String accessToken = updateTokenUseCase.updateInsertToken(user.providerId());
        return NetflixApiResponse.ok(accessToken);
    }

    // back-end 에서만 처리했을 시, successUri 처리
    // 코드 검증 및 accecss token 을 받을 후 유저 정보 조회를 security filter 가 해줌
    @GetMapping("/user/social-login/success")
    public NetflixApiResponse<String> socialLoginCallbackBackend(Authentication authentication) {

        OAuth2AuthenticationToken authenticationUser = (OAuth2AuthenticationToken) authentication;
        OAuth2User user = authenticationUser.getPrincipal();
        Map<String, Object> attributes = user.getAttributes();

        String id = attributes.get("id").toString();
        String username = Optional.ofNullable(attributes.get("nickname")).map(Object::toString).orElse("unknown");
        String provider = Optional.ofNullable(attributes.get("nickname")).map(Object::toString).orElse("unknown");

        UserResponse userByProviderId = fetchUserUseCase.findByProviderId(id);
        if (userByProviderId == null) {
            // 저장
            registerUserUseCase.registerSocialUser(
                username,
                provider,
                id
            );
        }
//
//        /* TODO: 2025-07-1, 화, 11:57 bmcho12
//         *  추후 비대칭키 변경
//         */
//        //서버내 secret key 로 server token 생성
        String accessToken = updateTokenUseCase.updateInsertToken(id);
        return NetflixApiResponse.ok(accessToken);
    }

}
