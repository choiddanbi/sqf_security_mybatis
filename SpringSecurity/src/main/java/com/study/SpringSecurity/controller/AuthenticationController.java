package com.study.SpringSecurity.Controller;

import com.study.SpringSecurity.aspect.annotation.ParamsAop;
import com.study.SpringSecurity.aspect.annotation.ValidAop;
import com.study.SpringSecurity.dto.request.ReqSigninDto;
import com.study.SpringSecurity.dto.request.ReqSignupDto;
import com.study.SpringSecurity.service.SigninService;
import com.study.SpringSecurity.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

//인증 관련 요청들은 요기에~
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private SignupService signupService;

    @Autowired
    private SigninService signinService;


    // 회원가입
    // @Valid 에서 reqdto 정규식 체크 -> reqdto 안에 false가 있으면 filederror객체 생성 -> bindingresult 생성해서 거기에 에러들 넣음
    // -> ValidAop 또는 ParamsAop 실행
    @ValidAop
    @ParamsAop
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody ReqSignupDto dto, BindingResult bindingResult) { //@Valid 와 BindingResult 는 짝꿍, @Valid 어노테이션으로 reqdto의 정규식 체크해줌
        // System.out.println(dto); // @ParamsAop 를 적어두면 sout 없어도 출력 가능 !!

        return ResponseEntity.created(null).body(signupService.signup(dto));
    }


    // 로그인
    @ValidAop
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody ReqSigninDto dto, BeanPropertyBindingResult bindingResult) {
        signinService.signin(dto);
        return ResponseEntity.ok().body(signinService.signin(dto));
    }
}
