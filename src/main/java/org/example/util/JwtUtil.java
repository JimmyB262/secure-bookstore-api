package org.example.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class JwtUtil {

    private static PublicKey publicKey;

    private static PrivateKey privateKey;

static {
    try {
        // Load private key from environment variable
        String privateKeyPem = System.getenv("JWT_PRIVATE_KEY");
        if (privateKeyPem == null) {
            throw new RuntimeException("JWT_PRIVATE_KEY env var not set");
        }
        privateKeyPem = privateKeyPem.replaceAll("-----\\w+ PRIVATE KEY-----", "")
                                   .replaceAll("\\s+", "");
        byte[] decodedPrivate = Base64.getDecoder().decode(privateKeyPem);
        PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(decodedPrivate);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        privateKey = kf.generatePrivate(privateSpec);

        // Load public key from environment variable
        String publicKeyPem = System.getenv("JWT_PUBLIC_KEY");
        if (publicKeyPem == null) {
            throw new RuntimeException("JWT_PUBLIC_KEY env var not set");
        }
        publicKeyPem = publicKeyPem.replaceAll("-----BEGIN PUBLIC KEY-----", "")
                                 .replaceAll("-----END PUBLIC KEY-----", "")
                                 .replaceAll("\\s+", "");
        byte[] decodedPublic = Base64.getDecoder().decode(publicKeyPem);
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(decodedPublic);
        publicKey = kf.generatePublic(publicSpec);
    } catch (Exception e) {
        throw new RuntimeException("Failed to load keys", e);
    }
}


    public static String generateToken(String username, List<String> roles) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .claim("groups", roles)
                .setIssuer("my-app")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(2400)))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public static Jws<Claims> validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .requireIssuer("my-app")
                .build()
                .parseClaimsJws(token);
    }
}
