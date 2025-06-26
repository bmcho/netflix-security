package com.bmcho.netflix.user;

import com.bmcho.netflix.user.command.UserResponse;

import java.util.Optional;

public interface FetchUserUserCase {
    UserResponse findByEmail(String email);
}
