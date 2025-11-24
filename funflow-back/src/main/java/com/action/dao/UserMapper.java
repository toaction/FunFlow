package com.action.dao;

import com.action.domain.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

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

    /**
     * 根据邮箱查询用户信息（包含所有状态的用户）
     *
     * @param email 邮箱地址（小写）
     * @return 用户实体
     */
    @Select("SELECT user_id, email, password_hash, avatar, nickname, username, " +
            "following_count, follower_count, cached_total_likes, bio, created_at, " +
            "last_login_at, status, deleted_at " +
            "FROM `user` WHERE email = #{email} AND deleted_at IS NULL")
    User selectByEmail(@Param("email") String email);

    /**
     * 插入用户记录
     *
     * @param user 用户实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO `user` (email, password_hash, avatar, nickname, username, " +
            "following_count, follower_count, cached_total_likes, bio, created_at, " +
            "last_login_at, status, deleted_at) " +
            "VALUES ( #{email}, #{passwordHash}, #{avatar}, #{nickname}, #{username}, " +
            "#{followingCount}, #{followerCount}, #{cachedTotalLikes}, #{bio}, #{createdAt}, " +
            "#{lastLoginAt}, #{status}, #{deletedAt})")
    int insert(User user);

    /**
     * 更新用户最后登录时间
     *
     * @param userId 用户ID
     * @param lastLoginAt 最后登录时间
     * @return 影响的行数
     */
    @Update("UPDATE `user` SET last_login_at = #{lastLoginAt} WHERE user_id = #{userId}")
    int updateLastLoginAt(@Param("userId") Long userId, @Param("lastLoginAt") LocalDateTime lastLoginAt);
}

