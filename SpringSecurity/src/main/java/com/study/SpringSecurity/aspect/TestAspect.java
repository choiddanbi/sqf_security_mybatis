package com.study.SpringSecurity.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// 이게 AOP 전체 문법!
@Aspect
@Component
@Order(value = 2)
public class TestAspect {

    @Pointcut("execution(* com.study.SpringSecurity.service.TestService.aop*(..))")
    private void pointCut() {} // 하나의 기능의 원하는 지점을 잘라서 그 안에 around()기능 넣기 ====> 전처리 -> 핵심기능 -> 후처리
    // 결과 : 전처리 -> AOP 테스트입니다~~ -> 후처리 -> AOP 테스트입니다.

    // ProceedingJoinPoint 를 매개변수로 받는 around 라는 메소드 생성
    @Around("pointCut()") //
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        System.out.println("전처리");
        Object result = proceedingJoinPoint.proceed(); //핵심기능 호출 , result 에 AOP 테스트 입니다~~ 담김 ,,,,,,,,,, 2하면
        System.out.println("후처리");

        return result;

    }
}
