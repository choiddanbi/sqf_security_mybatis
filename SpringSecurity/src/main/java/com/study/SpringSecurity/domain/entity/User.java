package com.study.SpringSecurity.domain.entity;

import com.study.SpringSecurity.security.principal.PrincipalUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor // select할떄 필요
@Entity // 테이블로 만들어줌
@Data
@Builder
public class User { // 얘가 테이블명이 됨

    @Id // 키값 설정 어노테이션 (PK)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement ( 키값 )
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false) // NotNull
    private String password;

    @Column(nullable = false)
    private String name;


    // fetch : 엔티티를 조인했을 때 연관된 데이터를 언제 가져올지 결정
    //  - EAGER : 당장 DB에서 가져옴 ( 데이터가 적을 때 사용 )
    //  - LAZY : 나중에 사용할 때 DB에서 가져옴 ( 데이터가 많을 때 사용, defalut 값)
    // cascade : 부모를 isert , update, delete 할 때 하위까지 같이 해주는거 // 외래키 쓰면 꼭 해줘야함
    //  -< user가 지워지면 role 도 같이 지워짐

    // 여기서는 '여러 user' 들이 '여러개의 role' 을 가질수 있으니까 다대다 사용중 -> 내 row 기준
    // 만약 '하나의 user' 가 '여러개의 role' 을 가진다면 일대다 -> 내 row 기준
    // USER 가 ROLE 을 가지는거니까 USER 가 부모 그러니까 User에 @ManyTOMany 랑 @JoinTable 은 짝꿍
    // 하나의 USER 에 여러 ROLE들이 들어가있는 형태다

    //ManyToMany + JoinTable을 하면 자동으로 만들어준 user-roles tb 인데 이러면 user=role id가 없음 그래서 아래처럼 OneToMany 써서 다시 만들어줄거야
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) // 테이블의 관계 여부에 따라 달라짐 -> 다대다 일대다 다대일 일대일
    @JoinTable( // CascadeType.ALL 가 있으면 알아서 save됨
            name = "user_roles", // 이 테이블을 생성할거임
            joinColumns = @JoinColumn(name = "user_id"), // 조인키값이 user_id ( Long id ) 임! 걍 그러려니 ~ JPA는 자동으로 컬럼명을 앞에 user_ 추가해서 만드니까,,? + user_id 가 같은 애들을 조인할거고
            inverseJoinColumns = @JoinColumn(name = "role_id") // 외래키는 role_id // user_id 가 일치하는 걔네들의 role_id를 가지고올거야
    )

    private Set<Role> roles; // 하나의 UESER 가 여러개의 권한(ROLE) 가질수도 있고 중복되면 안되니까 중복제거 하려구 set , Role 테이블을 조인할거다


    /* 내가 만드는 테이블
    @OneToMany(mappedBy = "user")
    private Set<UserRole> userRoles = new HashSet<>(); // select 를 위한 HashSet 생성-nullpoint 방지용
*/

    public PrincipalUser toPrincipalUser() {
        return PrincipalUser.builder()
                .userId(id)
                .username(username)
                .password(password)
                .roles(roles)
                .build();
    }

}
