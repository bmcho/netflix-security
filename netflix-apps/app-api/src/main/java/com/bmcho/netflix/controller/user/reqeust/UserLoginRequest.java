package com.bmcho.netflix.controller.user.reqeust;

import lombok.Getter;

@Getter
public class UserLoginRequest {
    private final String email;
    private final String password;

    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}