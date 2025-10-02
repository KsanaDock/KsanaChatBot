package com.ksanaDock.model.request;

import lombok.Data;

@Data
public class SendMessageRequest {
    private String receiverId;
    private String content;
    private String replyTo;
} 