package com.skronawi.tokenservice.jwt.core;

import com.skronawi.tokenservice.jwt.api.TokenService;
import com.skronawi.tokenservice.jwt.api.TokenServiceConfig;
import com.skronawi.tokenservice.jwt.api.TokenValidity;
import com.skronawi.tokenservice.jwt.api.UserInfo;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import java.util.Date;
import java.util.EnumSet;

public class TokenServiceImpl implements TokenService {

    private final TokenServiceConfig tokenServiceConfig;

    public TokenServiceImpl(TokenServiceConfig tokenServiceConfig) {
        this.tokenServiceConfig = tokenServiceConfig;
    }

    /*
    https://bitbucket.org/b_c/jose4j/wiki/JWT%20Examples
    "Producing and consuming a signed JWT", i.e. the token is NOT ENCRYPTED ! an example for encryption is also available!
     */
    public String buildToken(UserInfo userInfo) {

        try {
            JwtClaims claims = new JwtClaims();
//        claims.setIssuer("AuthService");  // who creates the token and signs it
//        claims.setAudience("Audience"); // to whom the token is intended to be sent
//        claims.setGeneratedJwtId(); // a unique identifier for the token
//        claims.setIssuedAtToNow();  // when the token was issued/created (now)
//        claims.setNotBeforeMinutesInThePast(2); // time before which the token is not yet valid (2 minutes ago)
//        claims.setClaim("email","mail@example.com"); // additional claims/attributes about the subject can be added
            claims.setExpirationTimeMinutesInTheFuture(tokenServiceConfig.getLifeTimeMinutes()); // time when the token will expire from now
            claims.setSubject(userInfo.getUsername()); // the subject/principal is whom the token is about
            claims.setStringListClaim("roles", userInfo.getRoles()); // multi-valued claims work too and will end up as a JSON array

            JsonWebSignature jws = new JsonWebSignature();
//            jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
            jws.setPayload(claims.toJson());
            jws.setKey(tokenServiceConfig.getRsaPrivateKey()); //   <------- private key! receiver must use the public key for decryption
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
            return jws.getCompactSerialization();

        } catch (JoseException je) {
            throw new IllegalArgumentException("problem with JWT/JWS", je);
        }
    }

    public TokenValidity validateToken(String token) {

        TokenValidity tokenValidity = new TokenValidity();

        JwtClaims claims;
        try {
            claims = getClaims(token, tokenServiceConfig);
        } catch (InvalidJwtException e) {
            tokenValidity.setInvalidities(EnumSet.of(TokenValidity.Invalidity.MALFORMED)); //TODO what about expired?
            return tokenValidity;
        }

        try {
            tokenValidity.setUsername(claims.getSubject());
            tokenValidity.setExpiryDate(new Date(claims.getExpirationTime().getValueInMillis()));
            tokenValidity.setRoles(claims.getStringListClaimValue("roles"));
        } catch (MalformedClaimException e) {
            tokenValidity = new TokenValidity();
            tokenValidity.setInvalidities(EnumSet.of(TokenValidity.Invalidity.MALFORMED)); //TODO what about expired?
            return tokenValidity;
        }

        tokenValidity.setValid(true);

        return tokenValidity;
    }

    /*
    this must also be done by the client!
     */
    private JwtClaims getClaims(String token, TokenServiceConfig config) throws InvalidJwtException {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
//                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
//                .setExpectedIssuer("Issuer") // whom the JWT needs to have been issued by
//                .setExpectedAudience("Audience") // to whom the JWT is intended for
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setRequireSubject() // the JWT must have a subject claim
                .setVerificationKey(config.getRsaPublicKey()) // verify the signature with the public key
                .build(); // create the JwtConsumer instance

        return jwtConsumer.processToClaims(token);
    }
}
