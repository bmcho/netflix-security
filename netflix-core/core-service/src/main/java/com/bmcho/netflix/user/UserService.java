package com.bmcho.netflix.user;

import com.bmcho.netfilx.user.CreateUser;
import com.bmcho.netfilx.user.FetchUserPort;
import com.bmcho.netfilx.user.InsertUserPort;
import com.bmcho.netfilx.user.UserPortResponse;
import com.bmcho.netflix.exception.UserException;
import com.bmcho.netflix.user.command.UserRegistrationCommand;
import com.bmcho.netflix.user.command.UserResponse;
import com.bmcho.netflix.user.response.UserRegistrationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements RegisterUserUseCase, FetchUserUseCase {

    private final InsertUserPort insertUserPort;
    private final FetchUserPort fetchUserPort;

    @Override
    public UserResponse findByEmail(String email) {

        Optional<UserPortResponse> userByEmail = fetchUserPort.findByEmail(email);
        if (userByEmail.isEmpty()) {
            throw new UserException.UserDoesNotExistException();
        }

        UserPortResponse userPortResponse = userByEmail.get();
        return UserResponse.builder()
            .userId(userPortResponse.userId())
            .email(userPortResponse.email())
            .password(userPortResponse.password())
            .phone(userPortResponse.phone())
            .role(userPortResponse.role())
            .userName(userPortResponse.userName())
            .build();
    }

    @Override
    public UserRegistrationResponse register(UserRegistrationCommand userRegistrationCommand) {
        String email = userRegistrationCommand.email();

        // 가입 되어 있는 회원임을 검증하기 위한 회원 조회
        Optional<UserPortResponse> userByEmail = fetchUserPort.findByEmail(email);

        // 기존 회원이 있으면 예외 던짐
        if (userByEmail.isPresent()) {
            throw new UserException.UserAlreadyExistException();
        }

        UserPortResponse userPortResponse = insertUserPort.create(
            CreateUser.builder()
                .username(userRegistrationCommand.username())
                .encryptedPassword(userRegistrationCommand.encryptedPassword())
                .email(userRegistrationCommand.email())
                .phone(userRegistrationCommand.phone())
                .build()
        );

        return new UserRegistrationResponse(
            userPortResponse.userName(),
            userPortResponse.email(),
            userPortResponse.phone()
        );
    }
}
