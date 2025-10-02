package com.ksanaDock.model.constant;

public class UserConstant {
    // 用户状态
    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_DISABLED = 0;
    
    // 性别
    public static final int GENDER_UNKNOWN = 0;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;
    
    // 登录相关
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTH_HEADER = "Authorization";
} 