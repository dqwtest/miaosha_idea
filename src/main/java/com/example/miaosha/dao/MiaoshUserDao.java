package com.example.miaosha.dao;

import com.example.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MiaoshUserDao {

    @Select("select * from miaosha_user where id = #{id}")
    public MiaoshaUser getById(@Param("id") long id);

    @Update("update miaosha_user set password = #{user.password} where id = #{user.id}  ")
    void update(@Param("user") MiaoshaUser toBeUpdate);
}
