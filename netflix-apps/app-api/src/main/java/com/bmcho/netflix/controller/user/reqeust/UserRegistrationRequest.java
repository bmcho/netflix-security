package com.bmcho.netflix.controller.user.reqeust;

import com.bmcho.netflix.annotation.PasswordEncryption;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRegistrationRequest {

    private final String username;

    @PasswordEncryption
    private String password;

    private final String email;
    private final String phone;
}
