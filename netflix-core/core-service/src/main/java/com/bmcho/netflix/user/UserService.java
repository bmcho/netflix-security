package com.bmcho.netflix.user;

import com.bmcho.netfilx.social.SocialPlatformPort;
import com.bmcho.netfilx.user.CreateUser;
import com.bmcho.netfilx.user.FetchUserPort;
import com.bmcho.netfilx.user.InsertUserPort;
import com.bmcho.netfilx.user.UserPortResponse;
import com.bmcho.netflix.exception.NetflixException;
import com.bmcho.netflix.exception.UserException;
import com.bmcho.netflix.user.command.UserRegistrationCommand;
import com.bmcho.netflix.user.command.UserResponse;
import com.bmcho.netflix.user.response.UserRegistrationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements RegisterUserUseCase, FetchUserUseCase {

    private final InsertUserPort insertUserPort;
    private final FetchUserPort fetchUserPort;
    private final List<SocialPlatformPort> socialPlatformPorts;

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
            .map(user ->  UserResponse.builder()
                .providerId(user.providerId())
                .provider(user.provider())
                .username(user.username())
                .build())
            .orElse(null);
    }

    @Override
    public UserResponse findUserAccessToken(String provider, String accessToken) {
        SocialPlatformPort selectedPort = socialPlatformPorts.stream()
            .filter(port -> port.supports(provider))
            .findFirst()
            .orElseThrow(NetflixException.NetflixUnsupportedSocialLoginException::new);
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
