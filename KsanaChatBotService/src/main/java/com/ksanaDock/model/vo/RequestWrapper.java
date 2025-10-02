package com.ksanaDock.model.vo;

import lombok.Data;

@Data
public class RequestWrapper {
    /**
     * 请求数据
     */
    private RequestData data;
    
    /**
     * 请求时间戳
     */
    private Long timestamp;
} 