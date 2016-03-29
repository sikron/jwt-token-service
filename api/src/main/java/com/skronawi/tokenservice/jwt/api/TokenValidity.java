package com.skronawi.tokenservice.jwt.api;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TokenValidity {

    private boolean isValid;
    private Set<Invalidity> invalidities;
    private String username;
    private List<String> roles;
    private Date expiryDate;

    public enum Invalidity {
        MALFORMED,
        EXPIRED,
        INVALID
    }

    public TokenValidity() {
        invalidities = new HashSet<>();
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public Set<Invalidity> getInvalidities() {
        return invalidities;
    }

    public void setInvalidities(Set<Invalidity> invalidities) {
        this.invalidities = invalidities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
