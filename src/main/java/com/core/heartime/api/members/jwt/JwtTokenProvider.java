package com.core.heartime.api.members.jwt;

import com.core.heartime.api.members.entity.Role;
import com.core.heartime.common.exception.UnauthorizedException;
import com.core.heartime.common.response.ErrorStatus;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKeyString;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email, Role role) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(email) // sub
                .claim("role", role.name()) // role 추가
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmail(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRole(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT token: " + e.getMessage());
            throw new UnauthorizedException(ErrorStatus.UNAUTHORIZED_TOKEN_EXPIRED.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Malformed JWT token: " + e.getMessage());
            throw new UnauthorizedException(ErrorStatus.UNAUTHORIZED_INVALID_TOKEN.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token: " + e.getMessage());
            throw new UnauthorizedException(ErrorStatus.UNAUTHORIZED_UNSUPPORTED_TOKEN.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Empty JWT token: " + e.getMessage());
            throw new UnauthorizedException(ErrorStatus.UNAUTHORIZED_EMPTY_TOKEN.getMessage());
        }
    }
}
