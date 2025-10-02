package com.ksanaDock.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.ksanaDock.constant.ResultCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult<T> {
    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 时间戳
     */
    private long timestamp;

    /**
     * 成功响应
     */
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(ResultCode.SUCCESS, "操作成功", data, true);
    }

    /**
     * 成功响应（带消息）
     */
    public static <T> ResponseResult<T> success(String message, T data) {
        return new ResponseResult<>(ResultCode.SUCCESS, message, data, true);
    }

    /**
     * 成功响应（无参数）
     */
    public static <T> ResponseResult<T> success() {
        return success(null);
    }

    /**
     * 失败响应
     */
    public static <T> ResponseResult<T> error(String message) {
        return new ResponseResult<>(ResultCode.INTERNAL_SERVER_ERROR, message, null, false);
    }

    /**
     * 参数错误响应
     */
    public static <T> ResponseResult<T> paramError(String message) {
        return new ResponseResult<>(ResultCode.PARAM_ERROR, message, null, false);
    }

    /**
     * 未授权响应
     */
    public static <T> ResponseResult<T> unauthorized(String message) {
        return new ResponseResult<>(ResultCode.UNAUTHORIZED, message, null, false);
    }

    /**
     * 禁止访问响应
     */
    public static <T> ResponseResult<T> forbidden(String message) {
        return new ResponseResult<>(ResultCode.FORBIDDEN, message, null, false);
    }

    /**
     * 资源不存在响应
     */
    public static <T> ResponseResult<T> notFound(String message) {
        return new ResponseResult<>(ResultCode.NOT_FOUND, message, null, false);
    }

    /**
     * 业务错误响应
     */
    public static <T> ResponseResult<T> businessError(String message) {
        return new ResponseResult<>(ResultCode.BUSINESS_ERROR, message, null, false);
    }

    /**
     * 自定义响应
     */
    public ResponseResult(Integer code, String message, T data, boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
        this.timestamp = System.currentTimeMillis();
    }
} 