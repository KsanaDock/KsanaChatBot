package com.ksanaDock.model.vo;

import lombok.Data;

@Data
public class ChatContactVO {
    private String contactId;
    private String nickname;
    private String avatar;
    private String openness;
    private String conscientiousness; 
    private String extraversion;
    private String agreeableness;
    private String neuroticism;
} 