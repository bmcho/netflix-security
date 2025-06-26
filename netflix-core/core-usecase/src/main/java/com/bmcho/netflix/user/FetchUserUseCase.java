package com.bmcho.netflix.user;

import com.bmcho.netflix.user.command.UserResponse;

public interface FetchUserUseCase {
    UserResponse findByEmail(String email);
}
