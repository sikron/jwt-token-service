package com.skronawi.tokenservice.jwt.api;

public class Token {

    //TODO workplace would need an expiration time, as the clients cannot decode the token
    //but the auth-service-client could decrypt this token

    private String token;

    //for deserialization, e.g. jackson
    public Token() {
    }

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
