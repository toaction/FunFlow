package com.action.funflow.dao;

import com.action.funflow.domain.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    /**
     * 根据邮箱查询用户是否存在
     *
     * @param email 邮箱地址（小写）
     * @return 用户数量
     */
    @Select("SELECT COUNT(1) FROM `users` WHERE email = #{email}")
    int countByEmail(@Param("email") String email);

    /**
     * 插入用户记录
     *
     * @param user 用户实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO `users` (email, password_hash, username, " +
            "avatar_url, nickname, bio, " +
            "following_count, follower_count, total_likes_received, " +
            "created_at, last_login_at, status) " +
            "VALUES (#{email}, #{passwordHash}, #{username}, " +
            "#{avatarUrl}, #{nickname}, #{bio}, " +
            "#{followingCount}, #{followerCount}, #{totalLikesReceived}, " +
            "#{createdAt}, #{lastLoginAt}, #{status})")
    int insert(User user);
}
