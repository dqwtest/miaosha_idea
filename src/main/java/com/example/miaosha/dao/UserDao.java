package com.example.miaosha.dao;

import com.example.miaosha.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {
    @Select("select * from user where id = #{id}")
    public User getById(int id);

    @Insert("insert into user(name) values(#{user.name})")
    public int insertUser(User user);
}
