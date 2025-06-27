package com.bmcho.netflix.security;

import com.bmcho.netflix.user.FetchUserUseCase;
import com.bmcho.netflix.user.command.UserResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NetflixUserDetailsService implements UserDetailsService {

    private final FetchUserUseCase fetchUserUseCase;

    /**
     * 파라미터로 전달받은 username(현재 프로젝트는 email을 주 ID로 간주) 으로 사용자를 조회 해야 한다.
     * - UseCase를 통해 User 정보 조회
     */
    @Override
    public NetflixAuthUser loadUserByUsername(String email) throws UsernameNotFoundException {
        UserResponse userResponse = fetchUserUseCase.findByEmail(email);
        return new NetflixAuthUser(
            userResponse.userId(),
            userResponse.userName(),
            userResponse.password(),
            userResponse.email(),
            userResponse.phone(),
            List.of(new SimpleGrantedAuthority(StringUtils.isBlank(userResponse.role()) ? "-" : userResponse.role()))
        );
    }
}
