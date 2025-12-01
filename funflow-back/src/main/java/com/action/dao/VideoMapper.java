package com.action.dao;

import com.action.domain.entity.Video;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短视频数据访问层
 *
 * @author Xiangfu
 */
@Mapper
public interface VideoMapper {

    /**
     * 插入视频记录
     *
     * @param video 视频实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO video (user_id, caption, tags, cover_url, video_url, status, is_public) " +
            "VALUES ( #{userId}, #{caption}, #{tags}, #{coverUrl}, #{videoUrl}, #{status}, #{isPublic})")
    int insert(Video video);
}
