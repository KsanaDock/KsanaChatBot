package com.ksanaDock.controller;

import com.ksanaDock.dao.AIProfileDao;
import com.ksanaDock.entity.AIProfile;
import com.ksanaDock.model.vo.ResponseResult;
import com.ksanaDock.service.AIProfileService;
import com.ksanaDock.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/aiProfile")
public class AIProfileController {
    
    private static final Logger log = LoggerFactory.getLogger(AIProfileController.class);
    
    @Autowired
    private AIProfileService aiProfileService;
    
    @Autowired
    private AIProfileDao aiProfileDao;
    
    @GetMapping("/getUserProfile")
    public ResponseResult getUserProfile(HttpServletRequest request) {
        // 从请求头获取用户ID，或使用默认值
        String userId = request.getHeader("userId");
        if (userId == null || userId.isEmpty()) {
            userId = "default_user";
        }
        
        try {
            AIProfile profile = aiProfileService.getUserProfile(userId);
            return ResponseResult.success(profile);
        } catch (Exception e) {
            LogUtil.logBusinessError("AIProfile", "getUserProfile", "Failed to get user profile", e);
            return ResponseResult.error("获取用户资料失败");
        }
    }
    
    @PostMapping("/saveUserProfile")
    public ResponseResult<Void> saveUserProfile(@RequestBody AIProfile profile, HttpServletRequest request) {
        try {
            // 从请求头获取用户ID，或使用默认值
            String userId = request.getHeader("userId");
            if (userId == null || userId.isEmpty()) {
                userId = "default_user";
            }
            
            profile.setUserId(userId);
            aiProfileService.saveUserProfile(profile);
            return ResponseResult.success(null);
        } catch (Exception e) {
            LogUtil.logBusinessError("AIProfile", "saveUserProfile", "Failed to save user profile", e);
            return ResponseResult.error("保存用户资料失败");
        }
    }
    
    @PostMapping("/uploadAvatar")
    public ResponseResult<Map<String, String>> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        try {
            // 从请求头获取用户ID，或使用默认值
            String userId = request.getHeader("userId");
            if (userId == null || userId.isEmpty()) {
                userId = "default_user";
            }
            
            // 简化的文件上传处理，直接返回成功
            Map<String, String> result = new HashMap<>();
            result.put("avatarUrl", "/uploads/avatars/" + userId + "_avatar.jpg");
            return ResponseResult.success(result);
        } catch (Exception e) {
            LogUtil.logBusinessError("AIProfile", "uploadAvatar", "Failed to upload avatar", e);
            return ResponseResult.error("上传头像失败");
        }
    }

    @PostMapping("/getAvatar")
    public ResponseResult<Map<String, String>> getAvatar(@RequestBody Map<String, String> params) {
        try {
            String userId = params.get("userId");
            return ResponseResult.success(aiProfileService.getUserAvatarUrl(userId));
        } catch (Exception e) {
            LogUtil.logBusinessError("AIProfile", "getAvatar", "获取头像失败", e);
            return ResponseResult.error("获取头像失败");
        }
    }

    @PostMapping("/checkNickname")
    public ResponseResult<Map<String, Boolean>> checkNickname(@RequestBody Map<String, String> params, HttpServletRequest request) {
        try {
            // 从请求头获取用户ID，或使用默认值
            String userId = request.getHeader("userId");
            if (userId == null || userId.isEmpty()) {
                userId = "default_user";
            }
            String nickname = params.get("nickname");
            boolean exists = aiProfileService.existsByNickname(nickname, userId);
            Map<String, Boolean> result = new HashMap<>();
            result.put("exists", exists);
            return ResponseResult.success(result);
        } catch (Exception e) {
            LogUtil.logBusinessError("AIProfile", "checkNickname", "检查昵称失败", e);
            return ResponseResult.error("检查昵称失败");
        }
    }

    @PostMapping("/updateNickname")
    public ResponseResult<?> updateNickname(@RequestBody Map<String, String> params, HttpServletRequest request) {
        try {
            // 从请求头获取用户ID，或使用默认值
            String userId = request.getHeader("userId");
            if (userId == null || userId.isEmpty()) {
                userId = "default_user";
            }
            
            String newNickname = params.get("nickname");
            aiProfileService.updateNickname(userId, newNickname);
            return ResponseResult.success(null);
        } catch (Exception e) {
            LogUtil.logBusinessError("AIProfile", "updateNickname", "Failed to update nickname", e);
            return ResponseResult.error("更新昵称失败");
        }
    }
}
