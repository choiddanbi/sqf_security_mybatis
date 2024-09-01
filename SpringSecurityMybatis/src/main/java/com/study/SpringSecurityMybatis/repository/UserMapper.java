package com.study.SpringSecurityMybatis.repository;

import com.study.SpringSecurityMybatis.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User findById(Long id);
    User findByUsername(String username);
    int save(User user);
    int deleteById(Long id);
}
