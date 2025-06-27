package com.bmcho.netflix.controller.user;

import com.bmcho.netflix.controller.NetflixApiResponse;
import com.bmcho.netflix.controller.user.reqeust.UserRegistrationRequest;
import com.bmcho.netflix.user.RegisterUserUseCase;
import com.bmcho.netflix.user.command.UserRegistrationCommand;
import com.bmcho.netflix.user.response.UserRegistrationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;

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
}
