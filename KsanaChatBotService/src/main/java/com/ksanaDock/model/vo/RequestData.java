package com.ksanaDock.model.vo;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class RequestData extends HashMap<String, Object> {
    
    public String getString(String key) {
        Object value = get(key);
        return value != null ? value.toString() : null;
    }
    
    public Integer getInteger(String key) {
        Object value = get(key);
        return value != null ? Integer.parseInt(value.toString()) : null;
    }
    
    public Long getLong(String key) {
        Object value = get(key);
        return value != null ? Long.parseLong(value.toString()) : null;
    }
    
    public Boolean getBoolean(String key) {
        Object value = get(key);
        return value != null ? Boolean.parseBoolean(value.toString()) : null;
    }
    
    public <T> T getObject(String key, Class<T> clazz) {
        Object value = get(key);
        // 这里可以使用 ObjectMapper 进行对象转换
        return value != null ? clazz.cast(value) : null;
    }
} 