package com.study.SpringSecurity.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// 이게 AOP 전체 문법!
@Aspect
@Component
@Order(value = 1) // 얘를 TestAspect1보다 먼저 실행해주기
public class TestAspect2 {

    @Pointcut("@annotation(com.study.SpringSecurity.aspect.annotation.Test2Aop)")
    private void pointCut() {} // 하나의 기능의 원하는 지점을 잘라서 그 안에 around()기능 넣기 ====> 전처리 -> 핵심기능 -> 후처리
    // 결과 : 전처리 -> AOP 테스트입니다~~ -> 후처리 -> AOP 테스트입니다.
    // -> 전처리 -> 전처리22 -> AOP 테스트2 입니다. -> 후처리22 -> 후처리

    // ProceedingJoinPoint 를 매개변수로 받는 around 라는 메소드 생성
    @Around("pointCut()") //proceedingJoinPoint 는 핵심기능 정보
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {


        CodeSignature signature = (CodeSignature) proceedingJoinPoint.getSignature();
        System.out.println(signature.getName()); // 메소드명 출력 -> aopTest2
        System.out.println(signature.getDeclaringTypeName()); // 메소드를 가지고 있는 경로.클래스명 출력

        Object[] args = proceedingJoinPoint.getArgs(); // 안의 값들 ex) 최단비 29
        String[] paramNames = signature.getParameterNames(); // name, age

        for(int i = 0; i < args.length; i++) {
            System.out.println(paramNames[i] + ": " + args[i]);
        }
        /*for(Object obj : proceedingJoinPoint.getArgs()){
            System.out.println(obj);
        }*/


        System.out.println("전처리22");
        Object result = proceedingJoinPoint.proceed(); // 핵심기능 호출인데 얘는 order때문에 TestAspect 로 감
        System.out.println("후처리22");

        return result;

    }
}
