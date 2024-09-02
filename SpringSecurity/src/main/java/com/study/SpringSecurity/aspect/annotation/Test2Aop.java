package com.study.SpringSecurity.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 실행중에 줄거다?
@Target({ElementType.METHOD}) // 메소드 위에 어노테이션을 달 수 있따
public @interface Test2Aop {
}
