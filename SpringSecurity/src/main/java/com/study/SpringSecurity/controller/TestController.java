package com.study.SpringSecurity.Controller;

import com.study.SpringSecurity.config.SecurityConfig;
import com.study.SpringSecurity.security.principal.PrincipalUser;
import com.study.SpringSecurity.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public ResponseEntity<?> get() {

        /*System.out.println(testService.aopTest()); // 얘가 핵심 기능
        // 얘를 기준으로 전/후가 실행됐음 좋겠따 -> 얘가 핵심기능 나머지 애들이 부가기능 -> 핵심기능만 두고 부가기능은 TestAspect 클래스로 분리 시켜둘거야
        // 만약 Pointcut 어노테이션에 testService.aopTest()가 있다면 Aspect의 around 먼저 실행 : controller -> aspect의 around
        // 결과 : 전처리 -> AOP 테스트입니다~~ -> 후처리 -> AOP 테스트입니다.

        testService.aopTest2("최단비", 29);
        // 결과 : 전처리 -> AOP 테스트입니다~~ -> 후처리 -> AOP 테스트입니다.
        // -> 전처리 -> 전처리22 -> AOP 테스트2 입니다. -> 후처리22 -> 후처리

        testService.aopTest3("010-1111-2222", "부산광역시");*/

        //authentication 객체를 가져와서 authentication 에 담음
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();

        return ResponseEntity.ok(principalUser);
    }
}
