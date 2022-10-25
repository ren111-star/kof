package com.kof.matchingsystem.repo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserRepo {
    @Select("select id from kof.user where username = #{username}")
    Long findIdByUsername (@Param("username") String username);

    @Select("select username from kof.user where id = #{id}")
    String findUsernameById (@Param("id") Long id);
}
