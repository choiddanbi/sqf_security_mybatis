package com.study.SpringSecurity.service;

import com.study.SpringSecurity.domain.entity.User;
import com.study.SpringSecurity.dto.request.ReqSigninDto;
import com.study.SpringSecurity.dto.response.RespJwtDto;
import com.study.SpringSecurity.repository.UserRepository;
import com.study.SpringSecurity.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


// 로그인 service
@Service
public class SigninService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    public RespJwtDto signin(ReqSigninDto dto) {

        // username 확인
        // dto 로 받아온걸로 user 엔티티로 가져옴
        User user =  userRepository.findByUsername(dto.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("사용자 정보를 다시 입력하세요.")
        );

        // password 확인
        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("사용자 정보를 다시 입력하세요.");
        }

        return RespJwtDto.builder()
                .accessToken(jwtProvider.generateUserToken(user))
                .build();
    }
}
