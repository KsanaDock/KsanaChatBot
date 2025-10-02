package com.ksanaDock.constant;

/**
 * 响应状态码常量
 */
public interface ResultCode {
    // 成功状态码
    int SUCCESS = 200;
    int CREATED = 201;
    int ACCEPTED = 202;
    int NO_CONTENT = 204;

    // 重定向状态码
    int MOVED_PERMANENTLY = 301;
    int FOUND = 302;
    int TEMPORARY_REDIRECT = 307;

    // 客户端错误状态码
    int BAD_REQUEST = 400;
    int UNAUTHORIZED = 401;
    int FORBIDDEN = 403;
    int NOT_FOUND = 404;
    int METHOD_NOT_ALLOWED = 405;
    int CONFLICT = 409;
    int PRECONDITION_FAILED = 412;
    int UNSUPPORTED_MEDIA_TYPE = 415;
    int TOO_MANY_REQUESTS = 429;

    // 服务器错误状态码
    int INTERNAL_SERVER_ERROR = 500;
    int NOT_IMPLEMENTED = 501;
    int BAD_GATEWAY = 502;
    int SERVICE_UNAVAILABLE = 503;
    int GATEWAY_TIMEOUT = 504;

    // 自定义业务状态码
    int PARAM_ERROR = 1001;           // 参数错误
    int VALIDATION_ERROR = 1002;      // 验证错误
    int BUSINESS_ERROR = 1003;        // 业务错误
    int DATABASE_ERROR = 1004;        // 数据库错误
    int UNAUTHORIZED_ERROR = 1005;    // 未授权错误
    int FORBIDDEN_ERROR = 1006;       // 禁止访问错误
    
    // 用户相关状态码 (2000-2999)
    int USER_NOT_FOUND = 2001;        // 用户不存在
    int USER_ALREADY_EXISTS = 2002;   // 用户已存在
    int PASSWORD_ERROR = 2003;        // 密码错误
    int ACCOUNT_LOCKED = 2004;        // 账号被锁定
    int LOGIN_ERROR = 2005;           // 登录失败
    
    // 权限相关状态码 (3000-3999)
    int TOKEN_EXPIRED = 3001;         // Token过期
    int TOKEN_INVALID = 3002;         // Token无效
    int PERMISSION_DENIED = 3003;     // 权限不足
    
    // 文件操作相关状态码 (4000-4999)
    int FILE_NOT_FOUND = 4001;        // 文件不存在
    int FILE_UPLOAD_ERROR = 4002;     // 文件上传失败
    int FILE_DOWNLOAD_ERROR = 4003;   // 文件下载失败
    int FILE_TYPE_ERROR = 4004;       // 文件类型错误
    
    // 第三方服务相关状态码 (5000-5999)
    int WECHAT_ERROR = 5001;          // 微信服务错误
    int API_ERROR = 5002;             // 外部API调用错误
    int SMS_ERROR = 5003;             // 短信服务错误
    
    // 系统维护相关状态码 (9000-9999)
    int SYSTEM_MAINTAINING = 9001;     // 系统维护中
    int SERVICE_DEGRADATION = 9002;    // 服务降级
}