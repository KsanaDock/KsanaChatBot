package com.ksanaDock.service;

import com.ksanaDock.model.request.SendMessageRequest;
import com.ksanaDock.model.response.SendMessageResponse;
import com.ksanaDock.model.vo.ChatContactVO;
import com.ksanaDock.entity.ChatMessage;

import java.util.List;

public interface ChatService {
    // 获取联系人列表
    List<ChatContactVO> getContacts(String userId);
    // 添加联系人
    void addContact(String userId, String contactId);
    // 发送消息给AI生成回复
    SendMessageResponse sendMessageToAI(String userId, SendMessageRequest request);
    // 获取历史记录
    List<ChatMessage> getHistory(String userId, String contactId);
    // 清空历史记录
    void clearHistory(String userId, String contactId);

    void deleteContact(String userId, String contactId);
} 