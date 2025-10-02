package com.ksanaDock.service;

import com.ksanaDock.entity.AIProfile;
import com.ksanaDock.model.vo.ResponseResult;

import java.util.Map;

public interface AIProfileService {
    /**
     * 获取用户个人资料
     *
     * @param userId 用户ID
     * @return 用户个人资料
     */
    AIProfile getUserProfile(String userId);

    /**
     * 获取用户头像
     *
     * @param userId 用户ID
     * @return 用户头像
     */
    Map<String, String> getUserAvatarUrl(String userId);

    /**
     * 保存用户个人资料
     *
     * @param profile 用户个人资料
     */
    void saveUserProfile(AIProfile profile);

    /**
     * 更新用户个人资料
     *
     * @param profile 用户个人资料
     */
    void updateUserProfile(AIProfile profile);

    /**
     * 保存用户头像
     *
     * @param avatarUrl 用户头像
     */
    void saveAvatar(String avatarUrl, String userId);

    /**
     * 检查昵称是否已存在
     *
     * @param nickname 昵称
     * @return 是否已存在
     */
    boolean existsByNickname(String nickname, String userId);

    /**
     * 更新昵称
     *
     * @param userId   用户ID
     * @param nickname 新昵称
     * @return 响应结果
     */
    ResponseResult<?> updateNickname(String userId, String nickname);
}