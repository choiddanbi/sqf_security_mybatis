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

// IOC 등록할건데 -> JwtProvider객체가 생성되어야함 -> String secret 매개변수가 필요한데 얘는 .yml 에서 들고옴
// 들고 온거로 JwtProvider 객체 생성 완료
@Component
public class JwtProvider {

    private final Key key;

    // @Value("${jwt.secret}")는 .yml의 jwt의 secret 값을 String 으로 가지고옴
    // bean을 등록하는 시점에 얘를 가지고옴
    // .yml 에 있는 secret을 암호화해서
    public JwtProvider(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); // 상수이기 떄문에 초기화 필수, secret값을 암호화해서 key에 넣는거임,
    }

    // 요청 들어온 token 이름에서 bearer 뗴어주는 메소드
    public String removeBearer(String token) {
        return token.substring("Bearer ".length()); // Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2Vy 에서 Bearer 잘라내기 + 앞에가 Bearer 인 애일떄만 잘라내기
    }

    // 토큰 생성 메소드인데 토큰은 문자열임
    public String generateUserToken(User user) {
        Date expireDate = new Date(new Date().getTime() + (1000l * 60 * 60 * 24 * 30)); // Date().getTime() 엄청 긴 값인데 new Date 에 넣어서 짧게 바뀜 , 한달기간으로 유지

        // .compact() 를 쓰지만 걍 빌더패턴임
        // Jwts 로 토큰의 key 를 암호화해서 String token 만들어줌
        String token = Jwts.builder()
                .claim("userId", user.getId()) // json 형태로 키밸루 넣어준느 용도 "userId": user.getId()
                .expiration(expireDate) // 토큰 만료시간 지정용 (30일)
                .signWith(key, SignatureAlgorithm.HS256) // key 값 맞는지 틀리는지 확인용 , ES256 알고릐즘으로 암호화할거야
                .compact();

        return token;
    }


    // signin 때 생성 암호화 된 String 토큰 을 복호화해서 Claims 객체모양으루
    public Claims parseToken(String token) {
        JwtParser jwtParser = Jwts.parser() // 암호화 된 토큰을 복호화
                                .setSigningKey(key) // 토큰 생성할때 생긴 key
                                .build(); // 키를 가지고 parser 를 생성한다.

        return jwtParser.parseClaimsJws(token).getPayload(); // 예외처리는 꼮 해줘야함 안해주면 필터 중간에 예외가 터지는거라 바로 응답 가버림
    }
}
