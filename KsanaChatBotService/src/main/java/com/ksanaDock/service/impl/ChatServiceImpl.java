package com.ksanaDock.service.impl;

import com.ksanaDock.constant.BusinessWords;
import com.ksanaDock.dao.ChatContactDao;
import com.ksanaDock.dao.ChatMessageDao;
import com.ksanaDock.entity.ChatContact;
import com.ksanaDock.entity.ChatMessage;
import com.ksanaDock.model.request.SendMessageRequest;
import com.ksanaDock.model.response.SendMessageResponse;
import com.ksanaDock.model.vo.ChatContactVO;
import com.ksanaDock.service.ChatService;
import com.ksanaDock.util.LogUtil;
import com.ksanaDock.util.DeepseekClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ChatServiceImpl implements ChatService {

    private static final String MODULE = "Chat";

    @Autowired
    private ChatContactDao chatContactDao;

    @Autowired
    private ChatMessageDao chatMessageDao;

    @Autowired
    private DeepseekClient deepseekClient;

    @Override
    public List<ChatContactVO> getContacts(String userId) {
        try {
            return chatContactDao.selectContactsByUserId(userId);
        } catch (Exception e) {
            LogUtil.logBusinessError(MODULE, "getContacts", "获取联系人列表失败", e);
            throw new RuntimeException("获取联系人列表失败");
        }
    }

    @Override
    public void addContact(String userId, String contactId) {
        try {
            // 检查是否已经是联系人
            List<ChatContactVO> existingContacts = chatContactDao.selectContactsByUserId(userId);
            boolean isExisting = existingContacts.stream()
                    .anyMatch(contact -> contact.getContactId().equals(contactId));

            if (!isExisting) {
                ChatContact contact = new ChatContact().setId(UUID.randomUUID().toString())
                        .setUserId(userId)
                        .setContactId(contactId);
                int result = chatContactDao.insert(contact);
                if (result <= 0) {
                    throw new RuntimeException("插入联系人记录失败");
                }
            }
        } catch (Exception e) {
            LogUtil.logBusinessError(MODULE, "addContact", "添加联系人失败", e);
            throw new RuntimeException("添加联系人失败: " + e.getMessage());
        }
    }

    @Override
    public SendMessageResponse sendMessageToAI(String userId, SendMessageRequest request) {
        try {
            // 保存用户发送的消息
            ChatMessage userMessage = new ChatMessage();
            userMessage.setId(UUID.randomUUID().toString());
            userMessage.setSenderId(userId);
            userMessage.setSenderType(BusinessWords.USER);
            userMessage.setReceiverId(request.getReceiverId());
            userMessage.setReceiverType(BusinessWords.AI);
            userMessage.setContent(request.getContent());
            userMessage.setReplyTo(request.getReplyTo());
            userMessage.setTimestamp(new Date());

            chatMessageDao.insert(userMessage);

            // 获取历史对话记录(目前仅支持查询自己本人与对方AI的聊天记录)
            List<ChatMessage> history = chatMessageDao.getHistory(userId, request.getReceiverId());

            // 构造AI回复 - 使用 Deepseek API
            String aiReply = deepseekClient.chat(
                    request.getContent(),
                    deepseekClient.convertHistoryToMessages(history, request.getReceiverId())
            );

            // 保存AI回复消息
            ChatMessage aiMessage = new ChatMessage();
            aiMessage.setId(UUID.randomUUID().toString());
            aiMessage.setSenderId(request.getReceiverId());
            aiMessage.setSenderType(BusinessWords.AI);
            aiMessage.setReceiverId(userId);
            aiMessage.setReceiverType(BusinessWords.USER);
            aiMessage.setContent(aiReply);
            aiMessage.setReplyTo(userMessage.getId());
            aiMessage.setTimestamp(new Date());

            chatMessageDao.insert(aiMessage);

            SendMessageResponse response = new SendMessageResponse();
            response.setMessageId(aiMessage.getId())
                    .setContent(aiReply)
                    .setTimestamp(aiMessage.getTimestamp())
                    .setReplyTo(aiMessage.getReplyTo())
                    .setSenderType(BusinessWords.AI)
                    .setReceiverType(BusinessWords.USER);

            return response;

        } catch (Exception e) {
            LogUtil.logBusinessError(MODULE, "sendMessage", "发送消息失败", e);
            throw new RuntimeException("发送消息失败");
        }
    }

    @Override
    public List<ChatMessage> getHistory(String userId, String contactId) {
        try {
            // 目前仅支持查询自己本人与对方AI的聊天记录
            return chatMessageDao.getHistory(userId, contactId);
        } catch (Exception e) {
            LogUtil.logBusinessError(MODULE, "getHistory", "获取聊天记录失败", e);
            throw new RuntimeException("获取聊天记录失败");
        }
    }

    @Override
    public void clearHistory(String userId, String contactId) {
        try {
            // 先更新引用关系
            chatMessageDao.updateReplyToNull(userId, contactId);
            // 再删除消息(目前仅支持删除自己本人与对方AI的聊天记录)
            chatMessageDao.clearHistory(userId, contactId);
        } catch (Exception e) {
            LogUtil.logBusinessError(MODULE, "clearHistory", "清空聊天记录失败", e);
            throw new RuntimeException("清空聊天记录失败");
        }
    }

    @Override
    public void deleteContact(String userId, String contactId) {
        try {
            // 先更新引用关系
            chatMessageDao.updateReplyToNull(userId, contactId);
            // 再删除消息(目前仅支持删除自己本人与对方AI的聊天记录)
            chatMessageDao.clearHistory(userId, contactId);
            // 最后删除联系人
            chatContactDao.delete(userId, contactId);
        } catch (Exception e) {
            LogUtil.logBusinessError(MODULE, "deleteContact", "删除联系人失败", e);
            throw new RuntimeException("删除联系人失败");
        }
    }
} 