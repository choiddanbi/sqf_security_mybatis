package com.study.SpringSecurity.security.jwt;

import com.study.SpringSecurity.domain.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key key;

    public JwtProvider(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String removeBearer(String token) {
        return token.substring("Bearer ".length());
    }

    public String generateUserToken(User user) {
        Date expireDate = new Date(new Date().getTime() + (1000l * 60 * 60 * 24 * 30));

        String token = Jwts.builder()
                .claim("userId", user.getId())
                .expiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public Claims parseToken(String token) {
        JwtParser jwtParser = Jwts.parser()
                .setSigningKey(key)
                .build();

        return jwtParser.parseClaimsJws(token).getPayload();
    }
}
