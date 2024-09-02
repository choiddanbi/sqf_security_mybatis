package com.study.SpringSecurity.repository;

import com.study.SpringSecurity.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { //<엔티티, ID자료형>

    Optional<User> findByUsername(String username); //  sql문 자동 작성해주는 문장!
}
