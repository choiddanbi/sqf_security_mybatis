package com.study.SpringSecurity.init;

import com.study.SpringSecurity.domain.entity.Role;
import com.study.SpringSecurity.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleDataLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    // 프로그램 실행하면 젤 먼저 무조건 실행되서 RULE 이라는 테이블이 생성됨
    @Override
    public void run(String... args) throws Exception { // 매개변수 갯수를 무제한으로 받을 수 있는데 배열로 받아옴

        // ROLE 테이블에 ROLE_USER 라는 컬럼이 없으면 TRUE
        if (roleRepository.findByName("ROLE_USER").isEmpty()){
            roleRepository.save(Role.builder().name("ROLE_USER").build());
        }

        if (roleRepository.findByName("ROLE_MANAGER").isEmpty()){
            roleRepository.save(Role.builder().name("ROLE_MANAGER").build());
        }

        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()){
            roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
        }
    }
}
