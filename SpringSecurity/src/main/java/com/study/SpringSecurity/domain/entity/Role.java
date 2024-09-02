package com.study.SpringSecurity.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity // 테이블 만들어주는 애
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; //ROLE_USER, ROLE_ADMIN, ROLE_MANAGER 이라는 권한 값들 담는 애

    /*@OneToMany(mappedBy = "role") // user-role 테이블의 role_id 랑 Role 테이블이랑 조인
    private Set<UserRole> userRoles = new HashSet<>(); // select 를 위한 HashSet 생성-nullpoint 방지용
    */

}
