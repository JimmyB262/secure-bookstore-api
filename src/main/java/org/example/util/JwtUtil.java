package org.example.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
            String publicKeyPem = System.getenv("MP_JWT_PUBLIC_KEY");
            if (publicKeyPem != null) {
                publicKeyPem = publicKeyPem
                        .replace("-----BEGIN PUBLIC KEY-----", "")
                        .replace("-----END PUBLIC KEY-----", "")
                        .replaceAll("\\s+", "");
                byte[] decoded = Base64.getDecoder().decode(publicKeyPem);
                X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                publicKey = kf.generatePublic(spec);
                System.out.println("Public key loaded successfully");
            } else {
                System.err.println("MP_JWT_PUBLIC_KEY environment variable not found.");
            }

            String privateKeyPem = System.getenv("MP_JWT_PRIVATE_KEY");
            if (privateKeyPem != null) {
                privateKeyPem = privateKeyPem
                        .replace("-----BEGIN PRIVATE KEY-----", "")
                        .replace("-----END PRIVATE KEY-----", "")
                        .replaceAll("\\s+", "");
                byte[] decoded = Base64.getDecoder().decode(privateKeyPem);
                PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                privateKey = kf.generatePrivate(spec);
                System.out.println("Private key loaded successfully");
            } else {
                System.out.println(" MP_JWT_PRIVATE_KEY not set. Token generation disabled.");
            }

        } catch (Exception e) {
            System.err.println(" Error loading JWT keys: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String generateToken(String username, List<String> roles) {
        System.out.println("Someone called me");
        if (privateKey == null) {
            throw new IllegalStateException("Private key not loaded. Cannot generate JWT.");
        }

        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .claim("groups", roles)
                .setIssuer("my-app")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(3600))) // 1 hour expiry
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public static Jws<Claims> validateToken(String token) {
        if (publicKey == null) {
            throw new IllegalStateException("Public key not loaded. Cannot validate JWT.");
        }

        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .requireIssuer("my-app")
                .build()
                .parseClaimsJws(token);
    }
}

