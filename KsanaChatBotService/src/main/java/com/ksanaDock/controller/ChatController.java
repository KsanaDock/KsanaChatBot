package com.ksanaDock.controller;

import com.ksanaDock.model.request.ContactOperateRequest;
import com.ksanaDock.model.request.SendMessageRequest;
import com.ksanaDock.model.response.SendMessageResponse;
import com.ksanaDock.model.vo.ChatContactVO;
import com.ksanaDock.entity.ChatMessage;
import com.ksanaDock.model.vo.ResponseResult;
import com.ksanaDock.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    
    @Autowired
    private ChatService chatService;

    // 根据用户id获取聊天联系人列表
    @GetMapping("/getContactsById")
    public ResponseResult<List<ChatContactVO>> getContacts(HttpServletRequest request) {
        // 从请求头获取用户ID，或使用默认值
        String userId = request.getHeader("userId");
        if (userId == null || userId.isEmpty()) {
            userId = "default_user";
        }
        return ResponseResult.success(chatService.getContacts(userId));
    }

    // 添加聊天联系人
    @PostMapping("/addContact")
    public ResponseResult<Void> addContact(
            HttpServletRequest request,
            @RequestBody ContactOperateRequest requestBody) {
        // 从请求头获取用户ID，或使用默认值
        String userId = request.getHeader("userId");
        if (userId == null || userId.isEmpty()) {
            userId = "default_user";
        }
        chatService.addContact(userId, requestBody.getContactId());
        return ResponseResult.success(null);
    }

    // 发送消息
    @PostMapping("/send")
    public ResponseResult<SendMessageResponse> sendMessage(
            HttpServletRequest request,
            @RequestBody SendMessageRequest requestBody) {
        // 从请求头获取用户ID，或使用默认值
        String userId = request.getHeader("userId");
        if (userId == null || userId.isEmpty()) {
            userId = "default_user";
        }
        SendMessageResponse response = chatService.sendMessageToAI(userId, requestBody);
        return ResponseResult.success(response);
    }

    // 获取聊天记录
    @GetMapping("/history")
    public ResponseResult<List<ChatMessage>> getChatHistory(
            HttpServletRequest request,
            @RequestParam String contactId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        // 从请求头获取用户ID，或使用默认值
        String userId = request.getHeader("userId");
        if (userId == null || userId.isEmpty()) {
            userId = "default_user";
        }
        List<ChatMessage> messages = chatService.getHistory(userId, contactId);
        return ResponseResult.success(messages);
    }

    // 清空历史记录(目前仅支持清除自己本人与对方AI的聊天记录)
    @PostMapping("/clearHistory")
    public ResponseResult<Void> clearHistory(
        HttpServletRequest request,
        @RequestBody ContactOperateRequest requestBody) {
        // 从请求头获取用户ID，或使用默认值
        String userId = request.getHeader("userId");
        if (userId == null || userId.isEmpty()) {
            userId = "default_user";
        }
        chatService.clearHistory(userId, requestBody.getContactId());
        return ResponseResult.success(null);
    }

    // 删除联系人
    @PostMapping("/deleteContact")
    public ResponseResult<Void> deleteContact(
        HttpServletRequest request,
        @RequestBody ContactOperateRequest requestBody) {
        // 从请求头获取用户ID，或使用默认值
        String userId = request.getHeader("userId");
        if (userId == null || userId.isEmpty()) {
            userId = "default_user";
        }
        chatService.deleteContact(userId, requestBody.getContactId());
        return ResponseResult.success(null);
    }
}