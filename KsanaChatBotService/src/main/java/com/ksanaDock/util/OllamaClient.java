package com.ksanaDock.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Component
public class OllamaClient {
    
    private static final String MODULE = "OllamaClient";
    
    @Value("${ollama.url}")
    private String ollamaUrl;
    
    private final RestTemplate restTemplate;
    
    public OllamaClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public String chat(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "qwen2.5");
            requestBody.put("prompt", prompt);
            requestBody.put("stream", false);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            Map<String, Object> response = restTemplate.postForObject(
                ollamaUrl + "/api/generate",
                request,
                Map.class
            );
            
            if (response != null && response.containsKey("response")) {
                return (String) response.get("response");
            }
            
            return "抱歉，我现在无法回复。";
            
        } catch (Exception e) {
            LogUtil.logBusinessError(MODULE, "chat", "调用Ollama服务失败", e);
            return "抱歉，我现在无法回复。";
        }
    }
} 