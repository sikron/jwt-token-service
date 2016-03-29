package com.skronawi.tokenservice.jwt.core;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/*
http://www.txedo.me/blog/java-generate-rsa-keys-write-pem-file/
http://www.txedo.me/blog/java-read-rsa-keys-pem-file/
*/
public class JwtKeyUtil {

    public static RsaJsonWebKey generateNewKeyPair() throws JoseException {
        return RsaJwkGenerator.generateJwk(2048);
    }

    public static String toString(RSAPrivateKey rsaPrivateKey) throws IOException {
        return toString("RSA PRIVATE KEY", rsaPrivateKey.getEncoded());
    }

    public static String toString(RSAPublicKey rsaPublicKey) throws IOException {
        return toString("RSA PUBLIC KEY", rsaPublicKey.getEncoded());
    }

    public static RSAPrivateKey fromPrivateString(String rsaPrivateKeyString) throws IOException {
        PemReader pemReader = new PemReader(new StringReader(rsaPrivateKeyString));
        PemObject pemObject = pemReader.readPemObject();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(pemObject.getContent());
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) kf.generatePrivate(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        }
    }

    public static RSAPublicKey fromPublicString(String rsaPublicKeyString) throws IOException {
        PemReader pemReader = new PemReader(new StringReader(rsaPublicKeyString));
        PemObject pemObject = pemReader.readPemObject();
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pemObject.getContent());
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) kf.generatePublic(pubKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String toString(String desc, byte[] bytes) throws IOException {
        PemObject pemObject = new PemObject(desc, bytes);
        StringWriter writer = new StringWriter();
        try (PemWriter pemWriter = new PemWriter(writer)) {
            pemWriter.writeObject(pemObject);
        }
        String toString = writer.toString();
        System.out.println(toString);
        return toString;
    }

    /*
    public static void main(String[] args) throws Exception {
        RsaJsonWebKey rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
        RSAPrivateKey rsaPrivateKey = rsaJsonWebKey.getRsaPrivateKey();
        RSAPublicKey rsaPublicKey = rsaJsonWebKey.getRsaPublicKey();
//        System.out.println(rsaPrivateKey);
//        System.out.println(rsaPublicKey);

        //------------------------write-----------------------
        //private key
        System.out.println("private key");
        String privateKeyString = write("RSA PRIVATE KEY", rsaPrivateKey.getEncoded());

        //public key
        System.out.println("public key");
        String publicKeyString = write("RSA PUBLIC KEY", rsaPublicKey.getEncoded());

        //------------------------read-----------------------
        //private
        RSAPrivateKey readRsaPrivateKey = readPrivate(privateKeyString);

        //public
        RSAPublicKey readRsaPublicKey = readPublic(publicKeyString);
    }

    private static RSAPublicKey readPublic(String publicKeyString) throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        PemReader pemReader = new PemReader(new StringReader(publicKeyString));
        PemObject pemObject = pemReader.readPemObject();
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pemObject.getContent());
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(pubKeySpec);
    }

    private static RSAPrivateKey readPrivate(String privateKeyString) throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        PemReader pemReader = new PemReader(new StringReader(privateKeyString));
        PemObject pemObject = pemReader.readPemObject();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(pemObject.getContent());
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) kf.generatePrivate(spec);
    }

    private static String write(String desc, byte[] bytes) throws IOException {
        PemObject pemObject = new PemObject(desc, bytes);
        StringWriter writer = new StringWriter();
        try (PemWriter pemWriter = new PemWriter(writer)) {
            pemWriter.writeObject(pemObject);
        }
        String toString = writer.toString();
        System.out.println(toString);
        return toString;
    }
*/
}
