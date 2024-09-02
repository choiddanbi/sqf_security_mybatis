package com.study.SpringSecurity.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 실행중에 적용시키겠따
@Target({ElementType.TYPE, ElementType.METHOD}) // TYPE = 클래스 위에 쓸 수 있다
public @interface ValidAop {

}
