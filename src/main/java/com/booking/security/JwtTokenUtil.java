package com.booking.security;

import com.booking.utils.ApplicationContextProvider;
import com.booking.utils.RequestHeaders;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class JwtTokenUtil {

    private static String secret;
    private static Long expiration;
    private static String header;

    // ====================== Extract JWT from Request ======================
    public static Optional<String> getRequestJwtToken() {
        String requestHeader = RequestHeaders.get(getAuthHeader());
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            return Optional.of(requestHeader.substring(7));
        }
        return Optional.empty();
    }

    // ====================== Get Username ======================
    public static String getUsernameFromToken(String token) {
        return token != null ? getClaimFromToken(token, Claims::getSubject) : null;
    }

    // ====================== Get any Claim ======================
    public static <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // ====================== Signing Key ======================
    private static SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(getSecret().getBytes(StandardCharsets.UTF_8));
    }

    // ====================== Parse All Claims ======================
    public static Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()                  // modern parser
                .verifyWith(getSigningKey())  // new way to set signing key
                .build()
                .parseClaimsJws(token)        // parses and verifies token
                .getBody();                   // returns Claims
    }

    // ====================== Generate JWT Token ======================
    public static String generateToken(JwtUserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("firstName", user.getFirstname());
        claims.put("lastName", user.getLastname());
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        return doGenerateToken(claims, user.getUsername());
    }

    private static String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + getExpiration() * 1000);

        return Jwts.builder()
                .claims(claims)               // modern non-deprecated
                .subject(subject)             // subject is still valid
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS512) // modern way
                .compact();
    }

    // ====================== Helpers ======================
    private static String getSecret() {
        if (secret == null) {
            secret = ApplicationContextProvider.getProperty("account.login.jwt.secret");
        }
        return secret;
    }

    private static Long getExpiration() {
        if (expiration == null) {
            expiration = Long.parseLong(ApplicationContextProvider.getProperty("account.login.jwt.expiration"));
        }
        return expiration;
    }

    private static String getAuthHeader() {
        if (header == null) {
            header = ApplicationContextProvider.getProperty("account.login.jwt.header");
        }
        return header;
    }

}
