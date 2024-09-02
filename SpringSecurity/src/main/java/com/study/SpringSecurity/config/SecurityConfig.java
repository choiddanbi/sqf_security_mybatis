package com.study.SpringSecurity.config;

import com.study.SpringSecurity.security.filter.JwtAccessTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// SecurityConfig 에 빨간줄 안뜨면 구현되지 않은(abstract) 메소드는 없다 -> ctrl + i 하면 아무것도 안나오고 바로 ctrl + o 해주면 됨
@EnableWebSecurity // 우리가 만든 SecurityConfig 를 적용시키겠다 !
@Configuration //IOC컨테이너에 BEAN 으로 들어가서 생성됨, 최초 셋팅이라고 티내기
public class SecurityConfig extends WebSecurityConfigurerAdapter { // WebSecurityConfigurerAdapter 를 상속받아서 구현중인 애가 있ㄴ느데 걔 말고 우리가 만들어서 쓰겠따~

    @Autowired
    private JwtAccessTokenFilter jwtAccessTokenFilter;

    // PW 암호화용
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 노란색 잠긴자물쇠 -> protected, 열린자물쇠 -> public
    // ctrl + o
    @Override // 재정의
    protected void configure(HttpSecurity http) throws Exception { // protected 인 configure 을 오벌아ㅣ드 할수 있는 이유는 -> extends WebSecurityConfigurerAdapter 상속받았기 떄문
        // super.configure(http); // 부모의 것들 가져와서 쓸 필요 없으니까 주석처리 ( 원래는 있는 애임 )
        http.formLogin().disable(); // 기본 제공된 formLogin ( 상속받아서 자기네들이 만든어 둔 애 ) 을 안쓰겠따 , 내가 추가작성 ( configure 안에는 formlogin 과 httpBasic 이 있음,  )
        http.httpBasic().disable(); // 내가 추가작성
        http.csrf().disable(); // csrf = 위조 방지 스티커(토큰), ssr에서 서버에서 만든 페이지가 정품인지 인증(?)용으로 사용, 인데 리액트에서는 사용 안해~

        // 세션을 사용하지 않는 방법 1
        // http.sessionManagement().disable();
        // 내가 추가작성
        // 스프링 시큐리티가 세션을 생성하지도 않고 기존의 세션을 사용하지도 않겠다


        // 세션을 사용하지 않는 방법 2
        // http.sesstionManagement().disable();
        // http.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 내가 추가작성
        // 스프링 시큐리티가 세션을 생성하지 않겠다. 기존의 세션을 완전히 사용하지 않겠다는 뜻은 아님 ( 세션을 살려는 두는데 시큐리티 안에서만 사용하지 않겠다 )
        // 보통 JWT 등의 토큰 인증방식을 사용할 때 설정하는 것

        http.cors(); // @crossorign 셋팅, 주소가 달라도 요청 주고받ㄷ을 수 있게
        http.authorizeRequests() // antMatchers 는 주소를 선택하는 애
                .antMatchers("/auth/**", "/h2-console/**") // 해당 주소의 애들은
                .permitAll() // 인증 절차 안해도 되고 !
                .anyRequest() // 그 외의 모든 요청들은
                .authenticated() // 인가(인증) 절차를 걸쳐라
                .and()
                .headers()
                .frameOptions()
                .disable();

        // UsernamePasswordAuthenticationFilter.class 전에
        // 내가 만든 jwtAccessTokenFilter 필터 실행 먼저 해라
        http.addFilterBefore(jwtAccessTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
