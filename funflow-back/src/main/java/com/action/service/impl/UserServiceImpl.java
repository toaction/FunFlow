package com.action.service.impl;

import com.action.exception.BusinessException;
import com.action.dao.UserMapper;
import com.action.domain.entity.User;
import com.action.domain.vo.UserVO;
import com.action.service.UserService;
import com.action.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 *
 * @author Xiangfu
 * @date 2025-11-26
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserVO getCurrentUser() {
        // 从ThreadLocal中获取当前用户ID
        Long userId = UserContext.getUserId();
        if (userId == null) {
            log.error("用户未登录");
            throw new BusinessException("用户未登录");
        }

        // 查询用户信息
        User user = userMapper.selectByUserId(userId);
        if (user == null) {
            log.error("用户不存在，用户ID: {}", userId);
            throw new BusinessException("用户不存在, 用户ID: " + userId);
        }

        // 转换为VO对象
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);

        log.info("成功获取用户信息，用户ID: {}", userId);
        return userVO;
    }
}