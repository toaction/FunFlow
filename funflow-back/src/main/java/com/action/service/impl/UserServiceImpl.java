package com.action.service.impl;

import com.action.domain.dto.UpdateProfileRequest;
import com.action.exception.BusinessException;
import com.action.dao.UserMapper;
import com.action.domain.entity.User;
import com.action.domain.vo.UserVO;
import com.action.service.OssService;
import com.action.service.UserService;
import com.action.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户服务实现类
 *
 * @author Xiangfu
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OssService ossService;

    @Override
    public UserVO getCurrentUser() {
        Long userId = checkLogin();

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

    @Override
    public String uploadAvatar(MultipartFile avatarFile) {
        Long userId = checkLogin();

        if (avatarFile == null || avatarFile.isEmpty()) {
            log.error("头像文件为空");
            throw new BusinessException("头像文件不能为空");
        }

        // 获取文件名和扩展名
        String originalFilename = avatarFile.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

        // 简单的格式检查
        if (!fileExtension.matches("jpg|jpeg|png|gif|webp")) {
            throw new BusinessException("不支持的图片格式，仅支持 JPG、PNG、GIF、WEBP");
        }

        // 构造唯一的文件名（避免重名）并包含扩展名
        String timestamp = String.valueOf(System.currentTimeMillis());
        String fileName = UserContext.getUserId() + "_avatar_" + timestamp + "." + fileExtension;

        try {
            // 上传图片到OSS
            String avatarUrl = ossService.uploadImage(avatarFile, fileName);

            // TODO: 头像审核
            // 这里先不进行数据库头像更新，待前端返回完整的用户信息后一同更新
            log.info("成功上传用户头像，用户ID: {}, 头像URL: {}", userId, avatarUrl);
            return avatarUrl;

        } catch (Exception e) {
            throw new BusinessException("上传头像失败: " + e.getMessage());
        }
    }

    @Override
    public void updateUserProfile(UpdateProfileRequest updateProfileRequest) {
        Long userId = checkLogin();

        // 查询当前用户信息
        User existingUser = userMapper.selectByUserId(userId);
        if (existingUser == null) {
            throw new BusinessException("用户不存在");
        }

        // 构建更新对象
        User updateUser = new User();
        BeanUtils.copyProperties(updateProfileRequest, updateUser);
        updateUser.setUserId(userId);

        // 执行更新
        int affectedRows = userMapper.updateProfile(updateUser);
        if (affectedRows == 0) {
            throw new BusinessException("更新用户信息失败");
        }

        log.info("成功更新用户信息，用户ID: {}", userId);
    }

    private Long checkLogin() {
        // 从ThreadLocal中获取当前用户ID
        Long userId = UserContext.getUserId();
        if (userId == null) {
            log.error("用户未登录");
            throw new BusinessException("用户未登录");
        }
        return userId;
    }
}
