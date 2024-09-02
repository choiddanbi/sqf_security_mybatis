package com.study.SpringSecurity.service;

import com.study.SpringSecurity.aspect.annotation.TimeAop;
import com.study.SpringSecurity.domain.entity.Role;
import com.study.SpringSecurity.domain.entity.User;
import com.study.SpringSecurity.dto.request.ReqSignupDto;
import com.study.SpringSecurity.repository.RoleRepository;
import com.study.SpringSecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

// 회원가입
@Service
@RequiredArgsConstructor //@Autowired 대신 final 이랑 같이해서 사용 가능
public class SignupService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @TimeAop // db에 save되는 애가 몇초인지 알려주는 aop
    @Transactional(rollbackFor = Exception.class) // 트렌젝션 실패 시(예외 터지면) 롤백해라 rollbackFor = Exception.class // 여기 클래스 안에서 예외 터지면 롤백
    public User signup(ReqSignupDto dto) {
        // 중간 테이블 (user_role) 만들기
        User user = dto.toEntity(passwordEncoder); // 비밀번호 암호화해서 들어감
        Role role = roleRepository.findByName("ROLE_USER").orElseGet( // db 에 ROLE 테이블 안에 ROLE_USER 값이 없다면 아래의 값을 get 해라 ( roleRepository 는 db에서 가져오라는거 )
                () -> roleRepository.save(Role.builder().name("ROLE_USER").build()) // ROLE_USER 생성해서 insert 하고 그걸 get 해라
        );

        user.setRoles(Set.of(role));
        user = userRepository.save(user); // USER 테이블에 AI user_id 들어감


       /*
       UserRole userRole = UserRole.builder()
                .user(user)
                .role(role)
                .build();

        userRole = userRoleRepository.save(userRole); // USERROLE 테이블에 들어감
        //user.setUserRoles(Set.of(userRole));
        */

        return user;
    }

    // 내장된 findByUsername가 없어서 내가 repository 에 만들어줌
    public boolean isDuplicatedUsername(String username) {
        return userRepository.findByUsername(username).isPresent(); // .isPresent() = 값이 null 인지 체크
    }
}
