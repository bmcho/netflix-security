package com.bmcho.netflix.user;

import com.bmcho.netflix.user.command.UserRegistrationCommand;
import com.bmcho.netflix.user.response.UserRegistrationResponse;

public interface RegisterUserUseCase {
    UserRegistrationResponse register(UserRegistrationCommand userRegistrationCommand);
}
