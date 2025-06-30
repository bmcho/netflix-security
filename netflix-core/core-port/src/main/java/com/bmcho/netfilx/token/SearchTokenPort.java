package com.bmcho.netfilx.token;

public interface SearchTokenPort {
    TokenPortResponse findByUserId(String userId);
}
