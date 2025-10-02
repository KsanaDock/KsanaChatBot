package com.ksanaDock.dao;

import com.ksanaDock.entity.Big5Personality;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface Big5PersonalityDao {
    /**
     * 插入大五人格数据
     */
    int insert(Big5Personality personality);

    /**
     * 根据用户ID查询最新的大五人格数据
     */
    Big5Personality selectLatestByUserId(String userId);

    /**
     * 更新大五人格数据
     */
    int update(Big5Personality personality);

    /**
     * 批量插入大五人格数据
     */
    int batchInsert(List<Big5Personality> personalities);

    /**
     * 获取所有用户的最新性格测试结果（分页）
     * @param offset 偏移量
     * @param size 每页大小
     * @param excludeIds 需要排除的用户ID列表
     */
    List<Big5Personality> selectAllLatestResults(@Param("offset") int offset, 
                                               @Param("size") int size, 
                                               @Param("excludeIds") List<String> excludeIds);

    /**
     * 统计用户总数（排除指定用户）
     */
    int countAllUsers(@Param("excludeIds") List<String> excludeIds);

    /**
     * 根据昵称查询最新的大五人格数据
     */
    Big5Personality selectLatestByNickname(String nickname);
}