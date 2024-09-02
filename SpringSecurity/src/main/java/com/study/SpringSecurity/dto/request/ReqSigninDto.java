package com.study.SpringSecurity.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class ReqSigninDto {
    
    @NotBlank(message = "사용자 이름을 입력하세요") // 빈값이 아니면 해당 message를 띄워라
    private String username;

    @NotBlank(message = "비밀번호를 입력하세요") // 빈값이 아니면 해당 message를 띄워라
    private String password;


}
