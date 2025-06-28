package com.bmcho.netflix.controller.user;

import com.bmcho.netflix.controller.NetflixApiResponse;
import com.bmcho.netflix.controller.user.reqeust.UserLoginRequest;
import com.bmcho.netflix.controller.user.reqeust.UserRegistrationRequest;
import com.bmcho.netflix.security.NetflixAuthUser;
import com.bmcho.netflix.user.RegisterUserUseCase;
import com.bmcho.netflix.user.command.UserRegistrationCommand;
import com.bmcho.netflix.user.response.UserRegistrationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthenticationManager authenticationManager;

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

//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(token);
        Authentication authentication = authenticationManager.authenticate(token);
        NetflixAuthUser netflixAuthUser = (NetflixAuthUser) authentication.getPrincipal();

        return NetflixApiResponse.ok("access-token");
    }

    @PostMapping("/user/callback")
    public NetflixApiResponse<String> kakaoLoginCallback(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        return NetflixApiResponse.ok(code);
    }

}
