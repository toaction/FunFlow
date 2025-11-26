package com.action.controller;

import com.action.common.Result;
import com.action.domain.vo.UserVO;
import com.action.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 * 处理用户信息相关接口
 * https://github.com/toaction/FunFlow/issues/9
 *
 * @author Xiangfu
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping()
    public Result<UserVO> getUser() {
        log.info("获取用户信息请求");
        UserVO userVO = userService.getCurrentUser();
        return Result.success(userVO);
    }
}