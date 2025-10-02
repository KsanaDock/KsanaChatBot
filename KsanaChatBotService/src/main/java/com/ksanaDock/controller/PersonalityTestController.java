package com.ksanaDock.controller;

import com.ksanaDock.entity.Big5Personality;
import com.ksanaDock.model.dto.PersonalityTestDTO;
import com.ksanaDock.model.vo.ResponseResult;
import com.ksanaDock.service.PersonalityTestService;
import com.ksanaDock.util.DeepseekClient;
import com.ksanaDock.util.LogUtil;
import com.ksanaDock.util.concurrent.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/personalityTest")
public class PersonalityTestController {

    private static final String MODULE = "PersonalityTest";

    @Autowired
    private PersonalityTestService personalityTestService;

    @Autowired
    private DeepseekClient deepseekClient;

    @PostMapping("/submit")
    public ResponseResult<Void> submitTest(@RequestBody PersonalityTestDTO testDTO, HttpServletRequest request) {
        try {
            // 从请求头获取用户ID，或使用默认值
            String userId = request.getHeader("userId");
            if (userId == null || userId.isEmpty()) {
                userId = "default_user";
            }
            
            Big5Personality big5TestResult = personalityTestService.saveTestResult(userId, testDTO.getPersonalityScores());
            // 异步生成并保存测试分析报告
            ThreadPoolManager.execute(() -> deepseekClient.generateTestReport(big5TestResult));
            return ResponseResult.success(null);
        } catch (Exception e) {
            LogUtil.logBusinessError("PersonalityTest", "submitTest", "Failed to submit test", e);
            return ResponseResult.error("提交测试失败，请稍后重试");
        }
    }

    @GetMapping("/latest")
    public ResponseResult<Big5Personality> getLatestResult(HttpServletRequest request) {
        try {
            // 从请求头获取用户ID，或使用默认值
            String userId = request.getHeader("userId");
            if (userId == null || userId.isEmpty()) {
                userId = "default_user";
            }
            
            Big5Personality result = personalityTestService.getLatestResult(userId);
            return ResponseResult.success(result);
        } catch (Exception e) {
            LogUtil.logBusinessError(MODULE, "getLatestResult", "Failed to get latest result", e);
            return ResponseResult.error("获取测试结果失败");
        }
    }

    @GetMapping("/allUsers")
    public ResponseResult<Map<String, Object>> getAllUsersPersonality(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String excludeIds) {
        try {
            List<String> excludeIdList = excludeIds != null ? 
                Arrays.asList(excludeIds.split(",")) : 
                Collections.emptyList();
            
            Map<String, Object> result = personalityTestService.getAllUsersLatestResult(page, size, excludeIdList);
            return ResponseResult.success(result);
        } catch (Exception e) {
            LogUtil.logBusinessError(MODULE, "getAllUsers", "Failed to get users data", e);
            return ResponseResult.error("获取用户数据失败，请稍后重试");
        }
    }

    @GetMapping("/searchByNickname")
    ResponseResult<Big5Personality> getPersonalityByNickname(@RequestParam String nickname) {
        try {
            Big5Personality personality = personalityTestService.getLatestByNickname(nickname);
            return ResponseResult.success(personality);
        } catch (Exception e) {
            LogUtil.logBusinessError(MODULE, "getPersonalityByNickname", "Failed to get personality by nickname", e);
            return ResponseResult.error("获取性格测试结果失败，请稍后重试");
        }
    }
}
