package com.skronawi.tokenservice.jwt.api;

public interface TokenService {

    /*
    so the app-logic first has to
        - get the credentials
        - analyse them according to auth-schema (basic, oauth, saml)
        - extract the user-info
        - find the user-info in the database (with roles and any extra-information)
        => now call buildToken(userinfo)

    so a separate credentialAnalyser module is needed, which supports
        - basic
        - oauth
        - saml
        - refresh-token
        => credentials -> UserInfo
    */

    String buildToken(UserInfo userInfo);

    TokenValidity validateToken(String token);

    //TODO getLifeCycle
}
