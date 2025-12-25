package com.zsgs.busbooking.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;


public class JwtUtil {

    private static final String SECRET_KEY = "MySecretKeyForJWTTokenGenerationMustBeLongEnough";
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private static final long EXPIRATION_TIME = 3600000; // 1 hour

    public static String generateToken(String mobileNumber,String role) {
        return Jwts.builder()
                .setSubject(mobileNumber)
                .claim("role", role) // Add roles to JWT claims
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String getRoleFromToken(String token) {
        Claims claims = validateToken(token);
        return claims.get("role", String.class); // Get directly as String
    }


    public static Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid or expired token", e);
        }
    }


    public static String getMobileNumerFromToken(String token) {
        Claims claims = validateToken(token);
        return claims.getSubject();
    }

    @SuppressWarnings("unchecked")
    public static List<String> getRolesFromToken(String token) {
        Claims claims = validateToken(token);
        return (List<String>) claims.get("roles");
    }

    public static boolean hasRole(String token, String role) {
        List<String> roles = getRolesFromToken(token);
        return roles != null && roles.contains(role);
    }

    public static boolean hasAnyRole(String token, String... requiredRoles) {
        List<String> userRoles = getRolesFromToken(token);
        if (userRoles == null) return false;

        for (String role : requiredRoles) {
            if (userRoles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = validateToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}