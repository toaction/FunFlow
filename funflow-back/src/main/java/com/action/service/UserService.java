package com.action.service;

import com.action.domain.vo.UserVO;

/**
 * 用户服务接口
 * 处理用户信息相关业务逻辑
 *
 * @author Xiangfu
 * @date 2025-11-26
 */
public interface UserService {

    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    UserVO getCurrentUser();
}