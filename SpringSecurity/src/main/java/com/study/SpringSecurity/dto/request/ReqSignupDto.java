package com.study.SpringSecurity.dto.request;

import com.study.SpringSecurity.domain.entity.Role;
import com.study.SpringSecurity.domain.entity.User;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
public class ReqSignupDto {

    @Pattern(regexp = "^[a-z0-9]{6,}$", message = "사용자 이름은 6자이상의 영소문자, 숫자 조합이어야 합니다.") // 이 정규식이 false면 filedError 객체 생성 ValidException_beanproperbindresult에 넣는다
    private String username;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*?])[A-Za-z\\d~!@#$%^&*?]{8,16}$",
            message = "비밀번호는 8자이상 16자이하의 영대소문, 숫자, 특수문자(~!@#$%^&*?)를 포함하여야합니다.") // 이 정규식이 false면 beanproperbindresult에 넣는다
    private String password;
    private String checkPassword;

    @Pattern(regexp = "^[가-힣]+$", message = "이름은 한글이어야합니다.")
    private String name;

    public User toEntity(BCryptPasswordEncoder passwordEncoder) {
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .name(name)
                .build();
    }
}
