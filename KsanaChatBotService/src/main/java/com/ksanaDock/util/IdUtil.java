package com.ksanaDock.util;

import java.util.UUID;

public class IdUtil {
    
    /**
     * 生成32位短UUID
     */
    public static String generateShortUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
} 