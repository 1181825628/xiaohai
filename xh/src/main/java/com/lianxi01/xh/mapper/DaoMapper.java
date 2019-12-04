package com.lianxi01.xh.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public class DaoMapper {
    @Update("update stu set password=#{password},email=#{email},xingming=#{xingming},sex=#{sex},shoujihao=#{shoujihao}")
    public void updateUser(@Param("password") String password,@Param("email") String email,@Param("xingming") String xingming,@Param("sex") String sex,@Param("shoujihao") String shoujihao) {
    }
}

