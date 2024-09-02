package com.study.SpringSecurity.security.filter;

import com.study.SpringSecurity.domain.entity.User;
import com.study.SpringSecurity.repository.UserRepository;
import com.study.SpringSecurity.security.jwt.JwtProvider;
import com.study.SpringSecurity.security.principal.PrincipalUser;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;


// 상속받으면 빨간 밑줄이 뜬다 -> 정의되지 않은 추상메소드가 있따 -> ctrl + i
@Component
public class JwtAccessTokenFilter extends GenericFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository; // db 에서 가져오는 값


    // 들어오는 모든 요청에 대해서 filter 동작 -> 토큰을 확인해주기 위한 filter
    // 요청 들어온 토큰을 가지고 복호화 해서 userId꺼냄, 그 userId로 db에서 user 가져오고,
    // user가 db에 있으면 다음 필터인 UsernamePasswordAuthenticationToken 실행
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String bearerAccessToken = request.getHeader("Authorization"); // 토큰값이 Authorization 에 들어있는데 이걸 꺼냄, 요청들어온 거의 토큰을 꺼내는데 (요청 = user 정보)
        if(bearerAccessToken != null) { // 토큰이 있을때만 동작해라
            String accessToken = jwtProvider.removeBearer(bearerAccessToken); // 토큰 앞의 bearer 지워주기 -> null이면 안됨
            Claims claims = null; // 예외ㅣ 안터졌을때

            try {
                claims = jwtProvider.parseToken(accessToken);
            } catch (Exception e) {
                filterChain.doFilter(servletRequest, servletResponse); // 다음 필터로 넘기기, 얘를 기준으로 위에가 전처리 아래가 후처리, doFilter를 아예 나가버림
                return;
            }

            Long userId = ((Integer) claims.get("userId")).longValue();


            Optional<User> optionalUser = userRepository.findById(userId); // db에서 꺼낸 값이니까 user 엔티티에 있는 내용을 담고있음
            if(optionalUser.isEmpty()) { // 토큰은 존재하지만 계정이 사라진 경우
                filterChain.doFilter(servletRequest, servletResponse); // 다음 필터로 넘기기, doFilter를 아예 나가버림
                return;
            }

            // principalUser.getAuthorities() 안에 권한들 전부가 들어있음
            PrincipalUser principalUser = optionalUser.get().toPrincipalUser(); //optionaalUser 에서 꺼내온건 db에서 꺼내온 user임

            // 얘를 실행하려면 principal이어야함
            // authentication객체 만들음
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalUser, principalUser.getPassword(), principalUser.getAuthorities());
            System.out.println("예외 발생하지 않음");


            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(servletRequest, servletResponse); // 다음꺼를 실행하라는 뜻인데 return이 없으니까 얘는 그냥 여기서 끝...
        // System.out.println(bearerAccessToken);

        // 이게 완료 되어야 인증이 됐다고 판단함 이게 pass 여야지 UsernamePasswordAuthenticationFilter 동작 다 패스여야지 ---> controller 동작 가능

    }
}
