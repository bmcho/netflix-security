package com.bmcho.netflix.user;

import com.bmcho.netfilx.social.SocialPlatformPort;
import com.bmcho.netfilx.user.CreateUser;
import com.bmcho.netfilx.user.FetchUserPort;
import com.bmcho.netfilx.user.InsertUserPort;
import com.bmcho.netfilx.user.UserPortResponse;
import com.bmcho.netflix.dispatcher.SocialPlatformDispatcher;
import com.bmcho.netflix.exception.UserException;
import com.bmcho.netflix.user.command.UserRegistrationCommand;
import com.bmcho.netflix.user.command.UserResponse;
import com.bmcho.netflix.user.response.UserRegistrationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserService implements RegisterUserUseCase, FetchUserUseCase {

    private final InsertUserPort insertUserPort;
    private final FetchUserPort fetchUserPort;
    private final SocialPlatformDispatcher socialPlatformDispatcher;

    /*
        이렇게 유저서칭에 대한걸 만들어 줄수 있으나 굳이..
     */
//    @Override
//    public UserResponse findUserByUserId(String userId) {
//        return findUserBy(userId, fetchUserPort::findByUserId);
//    }
//
//    @Override
//    public UserResponse findUserByEmail(String email) {
//        return findUserBy(email, fetchUserPort::findByEmail);
//    }
//
//    private UserResponse findUserBy(String searchParam, Function<String, Optional<UserPortResponse>> searchFunction) {
//        return searchFunction.apply(searchParam)
//            .map(this::mapToUserResponse)
//            .orElseThrow(UserException.UserDoesNotExistException::new);
//    }
//
//    private UserResponse mapToUserResponse(UserPortResponse userPortResponse) {
//        return UserResponse.builder()
//            .userId(userPortResponse.getUserId())
//            .email(userPortResponse.getEmail())
//            .password(userPortResponse.getPassword())
//            .phone(userPortResponse.getPhone())
//            .role(userPortResponse.getRole())
//            .username(userPortResponse.getUsername())
//            .build();
//    }

    @Override
    public UserResponse findUserByUserId(String userId) {
        Optional<UserPortResponse> userByEmail = fetchUserPort.findByUserId(userId);
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
            .username(userPortResponse.username())
            .build();
    }

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
            .username(userPortResponse.username())
            .build();
    }

    @Override
    public UserResponse findByProviderId(String providerId) {
        return fetchUserPort.findByProviderId(providerId)
            .map(user -> UserResponse.builder()
                .providerId(user.providerId())
                .provider(user.provider())
                .username(user.username())
                .role(user.role())
                .build())
            .orElse(null);
    }

    @Override
    public UserResponse findUserAccessToken(String provider, String accessToken) {
        SocialPlatformPort selectedPort = socialPlatformDispatcher.getProvide(provider);
        UserPortResponse user = selectedPort.findUserByAccessToken(accessToken);
        return UserResponse.builder()
            .username(user.username())
            .provider(user.provider())
            .providerId(user.providerId())
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
            userPortResponse.username(),
            userPortResponse.email(),
            userPortResponse.phone()
        );
    }

    @Override
    public UserRegistrationResponse registerSocialUser(String username, String provider, String providerId) {
        Optional<UserPortResponse> userByProviderId = fetchUserPort.findByProviderId(providerId);
        if (userByProviderId.isPresent()) {
            return null;
        }

        UserPortResponse socialUser = insertUserPort.createSocialUser(username, provider, providerId);
        return new UserRegistrationResponse(socialUser.username(), null, null);
    }
}
