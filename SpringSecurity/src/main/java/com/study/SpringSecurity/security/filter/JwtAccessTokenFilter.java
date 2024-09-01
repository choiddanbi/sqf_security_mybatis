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

@Component
public class JwtAccessTokenFilter extends GenericFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String bearerAccessToken = request.getHeader("Authorization");

        if(bearerAccessToken != null) {
            String accessToken = jwtProvider.removeBearer(bearerAccessToken);
            Claims claims = null;
            try {
                claims = jwtProvider.parseToken(accessToken);
            } catch (Exception e) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            Long userId = ((Integer) claims.get("userId")).longValue();
            Optional<User> optionalUser = userRepository.findById(userId);
            if(optionalUser.isEmpty()) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            PrincipalUser principalUser = optionalUser.get().toPrincipalUser();
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalUser, principalUser.getPassword(), principalUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

}
