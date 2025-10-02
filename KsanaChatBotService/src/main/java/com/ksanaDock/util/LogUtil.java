package com.ksanaDock.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.*;

public class LogUtil {
    private static final Logger log = LoggerFactory.getLogger(LogUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    // 使用 Java 8 兼容的方式创建不可变集合
    private static final Set<String> SENSITIVE_FIELDS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
        "password", "token", "phone", "email", "idCard", 
        "realName", "address", "birthday", "avatarUrl"
    )));
    
    /**
     * 记录业务错误
     */
    public static void logBusinessError(String module, String operation, String errorMsg, Throwable e) {
        log.error("[{}] {} failed: {}", module, operation, errorMsg, e);
    }
    
    /**
     * 记录系统错误
     */
    public static void logSystemError(String module, String operation, Throwable e) {
        log.error("[{}] System error in {}: {}", module, operation, e.getMessage(), e);
    }
    
    /**
     * 记录业务操作
     */
    public static void logBusinessOperation(String module, String operation, String result) {
        if (log.isDebugEnabled()) {
            log.debug("[{}] {} - {}", module, operation, result);
        }
    }
    
    /**
     * 记录带数据的业务操作
     */
    public static void logBusinessData(String module, String operation, Object data) {
        if (log.isDebugEnabled()) {
            try {
                String safeData = maskSensitiveInfo(data);
                log.debug("[{}] {} - Data: {}", module, operation, safeData);
            } catch (Exception e) {
                log.warn("[{}] Failed to log business data: {}", module, e.getMessage());
            }
        }
    }
    
    /**
     * 脱敏处理
     */
    private static String maskSensitiveInfo(Object data) throws Exception {
        if (data == null) return "null";
        
        if (data instanceof Map) {
            ObjectNode maskedNode = objectMapper.createObjectNode();
            ((Map<?, ?>) data).forEach((key, value) -> {
                String keyStr = String.valueOf(key);
                if (SENSITIVE_FIELDS.contains(keyStr)) {
                    maskedNode.put(keyStr, "***");
                } else if (value != null) {
                    maskedNode.put(keyStr, String.valueOf(value));
                }
            });
            return maskedNode.toString();
        }
        
        // 对象转换为JSON并脱敏
        ObjectNode node = objectMapper.valueToTree(data);
        SENSITIVE_FIELDS.forEach(field -> {
            if (node.has(field)) {
                node.put(field, "***");
            }
        });
        return node.toString();
    }
} 