package com.lianxi01.xh.dao;

import com.lianxi01.xh.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 用户数据访问接口
 * JpaRepository提供了基本的增删改查
 * JpaSpecificationExecutor用于复杂的条件查询
 */
public interface FangWenDao extends JpaRepository<user,String> , JpaSpecificationExecutor<user> {

}
