package com.ksanaDock.entity;

import lombok.Data;
import java.util.Date;

@Data
public class ChatMessage {
    /**
     * 消息ID
     */
    private String id;

    /**
     * 发送者ID
     */
    private String senderId;

    /**
     * 发送者类型（USER或AI）
     */
    private String senderType;

    /**
     * 接收者ID
     */
    private String receiverId;

    /**
     * 接收者类型（USER或AI）
     */
    private String receiverType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 回复消息的ID
     */
    private String replyTo;

    /**
     * 消息发送时间
     */
    private Date timestamp;
} 