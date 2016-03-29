package com.skronawi.tokenservice.jwt.api;

import java.util.List;

public interface UserInfo {

    String getUsername();

    void setUsername(String username);

    List<String> getRoles();

    void setRoles(List<String> roles);
}
