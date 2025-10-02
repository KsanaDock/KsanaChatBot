package com.ksanaDock.dao;

import com.ksanaDock.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserInfoDao {
    /**
     * 插入用户信息
     */
    int insert(UserInfo userInfo);

    /**
     * 更新用户信息
     */
    int update(UserInfo userInfo);

    /**
     * 更新用户昵称
     */
    void updateNickname(@Param("userId") String userId, @Param("nickname") String nickname);

    /**
     * 保存用户头像
     */
    void saveAvatar(@Param("avatarUrl") String avatarUrl, @Param("userId") String userId);

    /**
     * 根据ID查找用户信息
     */
    UserInfo findById(String id);

    /**
     * 根据用户账户ID查找用户信息
     */
    UserInfo findByUserAccountId(String userAccountId);

    /**
     * 根据昵称获取用户ID
     */
    String getUserIdByNickname(String nickname);

    /**
     * 根据用户ID获取头像路径
     */
    String getUserAvatarPath(String userId);
}