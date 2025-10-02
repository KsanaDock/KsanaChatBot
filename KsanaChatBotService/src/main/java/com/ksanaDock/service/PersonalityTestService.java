package com.ksanaDock.service;

import com.ksanaDock.entity.Big5Personality;

import java.util.List;
import java.util.Map;

public interface PersonalityTestService {
    /**
     * 保存性格测试结果
     */
    Big5Personality saveTestResult(String userId, Map<String, Integer> scores);

    /**
     * 获取用户最新的性格测试结果
     */
    Big5Personality getLatestResult(String userId);

    /**
     * 获取所有用户的最新性格测试结果（分页）
     * @param page 页码
     * @param size 每页大小
     * @param excludeIds 需要排除的用户ID列表
     * @return 包含分页信息和数据的Map
     */
    Map<String, Object> getAllUsersLatestResult(int page, int size, List<String> excludeIds);

    /**
     * 获取指定昵称的最新性格测试结果
     * @return 该昵称用户的大五人格测试结果
     */
    Big5Personality getLatestByNickname(String nickname);
}