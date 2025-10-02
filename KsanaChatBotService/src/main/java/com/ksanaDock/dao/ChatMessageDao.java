package com.ksanaDock.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ksanaDock.entity.ChatMessage;
import java.util.List;

@Mapper
public interface ChatMessageDao {
    int insert(ChatMessage message);

    /**
     * 根据用户ID和联系人ID获取聊天记录(目前仅支持查询自己本人与对方AI的聊天记录)
     * @param userId 用户ID
     * @param contactId 联系人ID
     * @return 聊天记录
     */
    List<ChatMessage> getHistory(
        @Param("userId") String userId,
        @Param("contactId") String contactId
    );
    
    void updateReplyToNull(
        @Param("userId") String userId,
        @Param("contactId") String contactId
    );

    /**
     * 根据用户ID和联系人ID删除聊天记录(目前仅支持删除自己本人与对方AI的聊天记录)
     * @param userId 用户ID
     * @param contactId 联系人ID
     */
    void clearHistory(
        @Param("userId") String userId,
        @Param("contactId") String contactId
    );
} 