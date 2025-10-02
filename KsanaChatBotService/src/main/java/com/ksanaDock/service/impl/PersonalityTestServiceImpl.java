package com.ksanaDock.service.impl;

import com.ksanaDock.dao.Big5PersonalityDao;
import com.ksanaDock.dao.UserInfoDao;
import com.ksanaDock.entity.Big5Personality;
import com.ksanaDock.service.PersonalityTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

@Service
public class PersonalityTestServiceImpl implements PersonalityTestService {
    
    @Autowired
    private Big5PersonalityDao big5PersonalityDao;

    @Autowired
    private UserInfoDao userInfoDao;
    
    @Override
    @Transactional
    public Big5Personality saveTestResult(String userId, Map<String, Integer> scores) {
        // 先查找是否存在已有的测试结果
        Big5Personality existingPersonality = big5PersonalityDao.selectLatestByUserId(userId);
        
        if (existingPersonality != null) {
            // 如果存在，更新现有数据
            existingPersonality.setNeuroticism(String.valueOf(scores.get("neuroticism")));
            existingPersonality.setOpenness(String.valueOf(scores.get("openness")));
            existingPersonality.setConscientiousness(String.valueOf(scores.get("conscientiousness")));
            existingPersonality.setExtraversion(String.valueOf(scores.get("extraversion")));
            existingPersonality.setAgreeableness(String.valueOf(scores.get("agreeableness")));
            
            int result = big5PersonalityDao.update(existingPersonality);
            if (result <= 0) {
                throw new RuntimeException("更新性格测试结果失败");
            }
            
            return existingPersonality;
        } else {
            // 如果不存在，创建新数据
            Big5Personality newPersonality = new Big5Personality(
                String.valueOf(scores.get("neuroticism")),
                String.valueOf(scores.get("openness")),
                String.valueOf(scores.get("conscientiousness")),
                String.valueOf(scores.get("extraversion")),
                String.valueOf(scores.get("agreeableness")),
                userId,
                UUID.randomUUID().toString()
            );
            
            int result = big5PersonalityDao.insert(newPersonality);
            if (result <= 0) {
                throw new RuntimeException("保存性格测试结果失败");
            }
            
            return newPersonality;
        }
    }
    
    @Override
    public Big5Personality getLatestResult(String userId) {
        Big5Personality personality = big5PersonalityDao.selectLatestByUserId(userId);
        if (personality == null) {
            throw new RuntimeException("未找到测试结果");
        }
        return personality;
    }
    
    @Override
    public Map<String, Object> getAllUsersLatestResult(int page, int size, List<String> excludeIds) {
        int offset = (page - 1) * size;
        List<Big5Personality> personalities = big5PersonalityDao.selectAllLatestResults(offset, size, excludeIds);
        int total = big5PersonalityDao.countAllUsers(excludeIds);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", personalities);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        
        return result;
    }

    @Override
    public Big5Personality getLatestByNickname(String nickname){
        if (nickname != null && !nickname.isEmpty()) {
            return big5PersonalityDao.selectLatestByNickname(nickname);
        }
        return null;
    }

}