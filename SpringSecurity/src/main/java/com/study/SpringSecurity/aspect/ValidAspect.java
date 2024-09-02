package com.study.SpringSecurity.aspect;

import com.study.SpringSecurity.dto.request.ReqSignupDto;
import com.study.SpringSecurity.exception.ValidException;
import com.study.SpringSecurity.service.SignupService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

@Slf4j
@Component // 이거때문에 service를 autowoired 가능
@Aspect
public class ValidAspect {

    @Autowired
    private SignupService signupService;

    @Pointcut("@annotation(com.study.SpringSecurity.aspect.annotation.ValidAop)")
    private void pointCut() {}


    // bindingResult 에러들이 들어가있음
    // proceedingJoinPoint : 핵심 로직 : controller !!
    // ProceedingJoinPoint 는 controller의 @Valid @RequestBody ReqSignupDto dto, BindingResult bindingResult
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable { //
        Object[] args = proceedingJoinPoint.getArgs(); // controller_signup메소드의 매개변수들 = @Valid @RequestBody ReqSignupDto dto, BindingResult bindingResult
        BeanPropertyBindingResult bindingResult = null; // 정규화의 오류 결과가 담겨져 있는 객체

        for(Object arg : args) { // args 들 중 bindingResult 를 찾기 위한 작업! ( ReqSignupDto 랑 BindingResult 중에 )
            if(arg.getClass() == BeanPropertyBindingResult.class) { // bindingResult 를 찾았으면 끝! ( BeanPropertyBindingResult.class 는 BindingResult )
                bindingResult = (BeanPropertyBindingResult) arg;
                break;
            }
        }

        // 핵심기능의 메소드중.메소드명을 가져온다.
        switch (proceedingJoinPoint.getSignature().getName()) { // signup()
            case "signup":
                validSignupDto(args, bindingResult);
                break;
        }

        // 그냥 적어주면 예외때문에 return 까지 못가서 controller 한테 응답을 못줌 -> 500에러 뜸
        // 클라이언트의 실수니까 400에러 보내줘야함 -> ExceptionControllerAdvice 생성
        if(bindingResult.hasErrors()) { // 모든 오류를 다 검사해서 있으면
            throw new ValidException("유효성 검사 오류", bindingResult.getFieldErrors()); // 오류들은 담은 ValidException() 객체를 만든다
        }
        return proceedingJoinPoint.proceed();
    } 

    private void validSignupDto(Object[] args, BeanPropertyBindingResult bindingResult) {
        for(Object arg : args) { //
            if (arg.getClass() == ReqSignupDto.class) { // ReqSignupDto 가 있으면 동작 = 회원가입용dto
                ReqSignupDto dto = (ReqSignupDto) arg;
                if (!dto.getPassword().equals(dto.getCheckPassword())) { // pw랑 checkpw가 다르면 실행 --> 사용자가 바로 요청한 갑ㅄ이라서 dto로 바로 꺼내쓰고
                    FieldError fieldError = new FieldError("checkPassword", "checkPassword", "비밀번호가 일치하지 않습니다.");
                    bindingResult.addError(fieldError); // bindingResult 주소에 addError해주는거라서 return 필요 없움
                }
                if(signupService.isDuplicatedUsername(dto.getUsername())) { // db에서 가져오는 결과값이라 service를 통해야하고
                    FieldError fieldError = new FieldError("username", "username", "이미 존재하는 사용자 이름입니다.");
                    bindingResult.addError(fieldError); // bindingResult 주소에 addError해주는거라서 return 필요 없움
                }
                break;
            }
        }
    }
}
