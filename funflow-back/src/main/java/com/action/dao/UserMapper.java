package com.action.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户数据访问层
 *
 * @author Xiangfu
 * @date 2025-11-20
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据邮箱查询用户是否存在
     * 查询条件：status = 1 AND deleted_at IS NULL
     *
     * @param email 邮箱地址（小写）
     * @return 用户数量
     */
    @Select("SELECT COUNT(1) FROM `user` WHERE email = #{email} AND status = 1 AND deleted_at IS NULL")
    int countByEmail(@Param("email") String email);
}

