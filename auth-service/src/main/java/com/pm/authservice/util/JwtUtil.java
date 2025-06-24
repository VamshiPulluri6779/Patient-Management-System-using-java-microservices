package com.pm.authservice.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        // Decode the Base64-encoded secret key provided from configuration
        byte[] decodedKey = Base64.getDecoder().decode(secretKey.getBytes(StandardCharsets.UTF_8));
        // Create a secure HMAC signing key based on the decoded bytes
        this.secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)                            // Set the token's subject (typically the user's id)
                .claim("role", role)                     // Add a custom claim with the user's role
                .expiration(new Date(System.currentTimeMillis() + 3600 * 1000 * 10)) // Set token expiration to 10 hours from now
                .issuedAt(new Date())                      // Record the current time as the issuance time
                .signWith(secretKey)                       // Sign the token with the secret key to ensure integrity
                .compact();                                // Build and return the compact JWT as a String
    }

    public void validateToken(String token) {
        try{
            // Validate the token using the secret key stored in server
            Jwts.parser().verifyWith((SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(token);
        } catch (JwtException e){
            throw new JwtException("Invalid JWT token");
        }
    }
}
