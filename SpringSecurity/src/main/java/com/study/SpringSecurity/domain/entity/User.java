package com.study.SpringSecurity.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.study.SpringSecurity.security.principal.PrincipalUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;

    // fetch: 엔티티를 조인했을 때 연관된 데이터를 언제 가져올지 결정(EAGER - 당장, LAZY - 나중에 사용할 때)
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

//    @OneToMany(mappedBy = "user")
//    private Set<UserRole> userRoles = new HashSet<>();

    public PrincipalUser toPrincipalUser() {
        return PrincipalUser.builder()
                .userId(id)
                .username(username)
                .password(password)
                .roles(roles)
                .build();
    }
}
