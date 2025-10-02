package com.ksanaDock.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

@Data
@Accessors(chain = true)
public class ChatContact {
    private String id;
    private String userId;
    private String contactId;
    private Date createTime;
    private Date updateTime;
    
    public ChatContact() {
        this.createTime = new Date();
        this.updateTime = new Date();
    }
} 