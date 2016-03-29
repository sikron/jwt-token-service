package com.skronawi.tokenservice.jwt.service;

import com.skronawi.tokenservice.jwt.api.TokenService;

public interface TokenServiceProvider {

    void init();

    void teardown();

    TokenService get();
}
