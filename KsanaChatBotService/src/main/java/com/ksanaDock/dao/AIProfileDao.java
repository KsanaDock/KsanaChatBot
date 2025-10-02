package com.ksanaDock.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ksanaDock.entity.AIProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AIProfileDao extends BaseMapper<AIProfile> {
    /**
     * 根据用户ID查询AI个人资料
     *
     * @param userId 用户ID
     * @return AI个人资料
     */
    AIProfile selectByUserId(@Param("userId") String userId);

    /**
     * 根据用户ID查询头像url
     *
     * @param userId 用户ID
     * @return AI头像url
     */
    String getAvatarUrlByUserId(@Param("userId") String userId);


    /**
     * 插入AI个人资料
     *
     * @param aiProfile AI个人资料
     * @return 影响的行数
     */
    int insert(AIProfile aiProfile);

    /**
     * 保存头像url
     *
     * @param avatarUrl 头像url
     */
    void saveAvatar(@Param("avatarUrl") String avatarUrl, @Param("userId") String userId);

    /**
     * 更新AI个人资料
     *
     * @param aiProfile AI个人资料
     * @return 影响的行数
     */
    int update(AIProfile aiProfile);

    /**
     * 更新AI个人资料中的昵称
     *
     * @param userId  用户ID
     * @param nickname 昵称
     * @return 影响的行数
     */
    void updateNickname(@Param("userId") String userId, @Param("nickname") String nickname);

    AIProfile findByUserId(String userId);
} 