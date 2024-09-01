package com.study.SpringSecurity.service;

import com.study.SpringSecurity.aspect.annotation.Test2Aop;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    public String aopTest() {
        System.out.println("AOP 테스트 입니다.");
        return "AOP 테스트 입니다.";
    }

    @Test2Aop
    public void aopTest2(String name, int age) {
        System.out.println("이름: " + name);
        System.out.println("나이: " + age);
        System.out.println("AOP 테스트2 입니다.");
    }

    @Test2Aop
    public void aopTest3(String phone, String address) {
        System.out.println("AOP 테스트3 입니다.");
    }
}
