package com.skronawi.tokenservice.jwt.api;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class TokenServiceConfig {

    private long lifeTimeMinutes;
    private RSAPrivateKey rsaPrivateKey;
    private RSAPublicKey rsaPublicKey;

    public long getLifeTimeMinutes() {
        return lifeTimeMinutes;
    }

    public void setLifeTimeMinutes(long lifeTimeMinutes) {
        this.lifeTimeMinutes = lifeTimeMinutes;
    }

    public RSAPrivateKey getRsaPrivateKey() {
        return rsaPrivateKey;
    }

    public void setRsaPrivateKey(RSAPrivateKey rsaPrivateKey) {
        this.rsaPrivateKey = rsaPrivateKey;
    }

    public RSAPublicKey getRsaPublicKey() {
        return rsaPublicKey;
    }

    public void setRsaPublicKey(RSAPublicKey rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
    }
}
