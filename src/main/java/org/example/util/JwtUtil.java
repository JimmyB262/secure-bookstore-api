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
        // Load private key from PEM file
        try (InputStream is = JwtUtil.class.getResourceAsStream("/private_key.pem")) {
            String key = new String(is.readAllBytes(), StandardCharsets.UTF_8)
                    .replaceAll("-----\\w+ PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            byte[] decoded = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            privateKey = kf.generatePrivate(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load private key", e);
        }

        try (InputStream is = JwtUtil.class.getResourceAsStream("/public_key.pem")) {
            if (is == null) {
                throw new RuntimeException("public_key.pem not found in resources");
            }
            String key = new String(is.readAllBytes(), StandardCharsets.UTF_8)
                    .replaceAll("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");
            byte[] decoded = Base64.getDecoder().decode(key);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            publicKey = kf.generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load public key", e);
        }
    }

    public static String generateToken(String username, List<String> roles) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .claim("groups", roles)
                .setIssuer("my-app")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(600)))
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
