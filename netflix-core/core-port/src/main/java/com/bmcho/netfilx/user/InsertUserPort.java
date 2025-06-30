package com.bmcho.netfilx.user;

public interface InsertUserPort {
    UserPortResponse create(CreateUser createUser);

    UserPortResponse createSocialUser(String username, String provider, String providerId);
}
