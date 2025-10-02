package com.ksanaDock.service.impl;

import com.ksanaDock.constant.BusinessWords;
import com.ksanaDock.dao.AIProfileDao;
import com.ksanaDock.dao.UserInfoDao;
import com.ksanaDock.entity.AIProfile;
import com.ksanaDock.model.vo.ResponseResult;
import com.ksanaDock.service.AIProfileService;
import com.ksanaDock.service.FileUploadService;
import com.ksanaDock.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Service
public class AIProfileServiceImpl implements AIProfileService {
    @Autowired
    private AIProfileDao aiProfileDao;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public AIProfile getUserProfile(String userId) {
        try {
            AIProfile aiProfile = aiProfileDao.selectByUserId(userId);
            if (aiProfile == null) {
                return null;
            }
            aiProfile.setAvatar(fileUploadService.generateFullOssUrl(aiProfile.getAvatar()));
            return aiProfile;
        } catch (Exception e) {
            LogUtil.logBusinessError("AIProfile", "getUserProfile", "查询用户个人资料失败", e);
            throw e;
        }
    }

    @Override
    public Map<String, String> getUserAvatarUrl(String userId) {
        // 获取存储的对象路径
        String objectName = aiProfileDao.getAvatarUrlByUserId(userId);
        if (objectName == null || objectName.isEmpty()) {
            objectName = userInfoDao.getUserAvatarPath(userId);
        }
        
        // 生成OssURL
        String signedUrl = fileUploadService.generateFullOssUrl(objectName);
        Map<String, String> data = new HashMap<>();
        data.put("url", signedUrl != null ? signedUrl : "");  // 只返回OssURL
        return data;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserProfile(AIProfile profile) {
        try {
            // 验证昵称
            if (profile.getNickname() != null && !profile.getNickname().isEmpty()) {
                checkAndSaveNickname(profile.getNickname(), profile.getUserId());
            }

            AIProfile existingProfile = aiProfileDao.selectByUserId(profile.getUserId());
            // 保存资料的时候不需要再修改头像的url,否则会把其他缓存的图片url的地址给保存下来了，应该是通过其他接口来更新这个头像的url
            if (existingProfile == null) {
                aiProfileDao.insert(profile);
            } else {
                profile.setId(existingProfile.getId());
                aiProfileDao.update(profile);
            }
        } catch (Exception e) {
            LogUtil.logBusinessError("AIProfile", "saveUserProfile", "保存用户个人资料失败", e);
            throw e;
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserProfile(AIProfile profile) {
        try {
            profile.setUpdateTime(LocalDateTime.now());
            aiProfileDao.update(profile);
        } catch (Exception e) {
            LogUtil.logBusinessError("AIProfile", "updateUserProfile", "更新用户个人资料失败", e);
            throw e;
        }
    }

    @Override
    public void saveAvatar(String objectName, String userId) {
        try {
            if (aiProfileDao.findByUserId(userId) == null) {
                AIProfile aiProfile = new AIProfile();
                aiProfile.setUserId(userId);
                aiProfile.setAvatar(objectName); // 存储对象路径而不是URL
                aiProfileDao.insert(aiProfile);
            } else {
                String oldObjectName = aiProfileDao.getAvatarUrlByUserId(userId);
                if (oldObjectName != null && !oldObjectName.isEmpty()) {
                    fileUploadService.deleteFile(oldObjectName);
                }
                aiProfileDao.saveAvatar(objectName, userId);
            }
            userInfoDao.saveAvatar(objectName, userId);
        } catch (Exception e) {
            LogUtil.logBusinessError("AIProfile", "saveAvatar", "保存用户头像失败", e);
            throw e;
        }
    }

    @Override
    public boolean existsByNickname(String nickname, String userId) {
        String idByNickname = userInfoDao.getUserIdByNickname(nickname);
        return idByNickname != null && !idByNickname.isEmpty() && !idByNickname.equals(userId);
    }

    @Override
    public ResponseResult<?> updateNickname(String userId, String nickname) {
        return checkAndSaveNickname(nickname, userId);
    }

    private ResponseResult<?> checkAndSaveNickname(String nickname, String userId) {
        // 长度检查
        if (nickname.length() > 10 || nickname.length() < 2) {
            return ResponseResult.error(BusinessWords.NICKNAME_LENGTH_RULE);
        }

        // 字符检查
        if (!nickname.matches("^[\\u4e00-\\u9fa5a-zA-Z0-9]+$")) {
            return ResponseResult.error(BusinessWords.NICKNAME_CHARACTER_RULE);
        }
        // 检查昵称是否重复
        if (existsByNickname(nickname, userId)) {
            return ResponseResult.error(BusinessWords.NICKNAME_EXIST_RULE);
        }
        // 检查修改时间间隔
        AIProfile existingUser = aiProfileDao.selectByUserId(userId);
        if (existingUser != null && existingUser.getNicknameUpdateTime() != null) {
            long lastUpdate = existingUser.getNicknameUpdateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long now = System.currentTimeMillis();
            if (now - lastUpdate < 30 * 24 * 60 * 60 * 1000L) {
                return ResponseResult.error(BusinessWords.NICKNAME_UPDATE_RULE);
            }
        }
        // 更新昵称和修改时间,资料表和信息表里面的昵称字段都需要更新
        userInfoDao.updateNickname(userId, nickname);
        aiProfileDao.updateNickname(userId, nickname);
        return ResponseResult.success();
    }
}