package com.epam.xstack.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    @Getter
    private long validityInMilliseconds;

    public String createToken(Authentication authentication) {
        String username = authentication.getName();
        Map<String, Object> claims = new HashMap<>();
        claims.put("auth", authentication.getAuthorities());
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .compact();
    }

}

