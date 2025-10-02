package com.ksanaDock.model.response;

import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

@Data
@Accessors(chain = true)
public class SendMessageResponse {
    private String messageId;
    private String content;
    private Date timestamp;
    private String replyTo;
    private String senderId;
    private String senderType;
    private String receiverId;
    private String receiverType;
} 