package com.study.SpringSecurity.security.principal;

import com.study.SpringSecurity.domain.entity.Role;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Builder
@Data
// PrincipalUser 안에는 username과 password 등등을 기본으로 가지고있음
public class PrincipalUser implements UserDetails {

    private Long userId;
    private String username;
    private String password;
    private Set<Role> roles;

    @Override // GrantedAuthority 객체를 상속받은 애만 Collection<>에 담길 수 잇음
    public Collection<? extends GrantedAuthority> getAuthorities() {
        /* 아래 return 코드와 동일한 코드!!
        Set<GrantedAuthority> authorities = new HashSet<>();

        for(Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;*/


        return roles.stream() // roles 라는 collect 을 stream 으로 바꿔서 map 돌릴거임 ( map => 하나씩 꺼내서 새로운걸로 바꿔서 새로운 배열에 담는다 )
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet()); // 그리고 그 stream 을 Set collect로 바꿔준다
    }

    @Override
    public boolean isAccountNonExpired() { // 만료된 계정 ( ex, 임시 계정 )
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { // 잠긴 계정 (ex, 비번을 5번 이상 틀림 또는 휴먼계정 )
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { // 인증이 만료된 계정 ( pw를 n년 이상 안바꿨음 )
        return true;
    }

    @Override
    public boolean isEnabled() { // 활성화 유/무
        return true;
    }
}
