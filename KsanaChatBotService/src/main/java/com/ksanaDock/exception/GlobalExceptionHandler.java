package com.ksanaDock.exception;

import com.ksanaDock.model.vo.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataAccessException;
import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseResult<String> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage(), e);
        return ResponseResult.error(e.getMessage());
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public ResponseResult<String> handleSQLException(Exception e) {
        log.error("数据库操作异常: {}", e.getMessage(), e);
        return ResponseResult.error("系统繁忙，请稍后重试");
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult<String> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return ResponseResult.error("系统繁忙，请稍后重试");
    }
} 