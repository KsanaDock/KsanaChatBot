package com.ksanaDock.util;

import com.ksanaDock.dao.Big5PersonalityDao;
import com.ksanaDock.entity.Big5Personality;
import com.ksanaDock.service.AIProfileService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.ksanaDock.entity.ChatMessage;
import com.ksanaDock.entity.AIProfile;

import java.util.*;

@Component
public class DeepseekClient {

    private static final String MODULE = "DeepseekClient";
    private static final String API_URL = "https://api.deepseek.com/chat/completions";
    // DeepSeek-Chat 模型的最大上下文长度限制
    private static final int MAX_CONTEXT_LENGTH = 16384;  // 根据实际模型参数设置
    // 为system prompt和用户新消息预留的token数
    private static final int RESERVED_TOKENS = 1000;
    // 保留最近的消息数量
    private static final int MAX_MESSAGES = 20;

    @Value("${deepseek.api-key}")
    private String apiKey;

    @Autowired
    private Big5PersonalityDao big5PersonalityDao;

    @Autowired
    private AIProfileService aiProfileService;

    private final RestTemplate restTemplate;

    public DeepseekClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 估算文本的token数量（简单实现，实际token数可能与此不同）
    private int estimateTokens(String text) {
        if (text == null) return 0;
        // 一个粗略的估算：假设平均每个字符占用1个token
        // 实际应该使用tiktoken或类似的分词器来计算
        return text.length();
    }

    // 估算消息列表的总token数
    private int estimateTotalTokens(List<Map<String, String>> messages) {
        return messages.stream()
            .mapToInt(msg -> estimateTokens(msg.get("content")))
            .sum();
    }

    public String chat(String prompt, List<Map<String, String>> history) {
        try {
            // 检查当前prompt的token数
            int promptTokens = estimateTokens(prompt);
            if (promptTokens > MAX_CONTEXT_LENGTH - RESERVED_TOKENS) {
                return "消息太长，请缩短后重试。";
            }

            // 检查历史消息的总token数
            int historyTokens = estimateTotalTokens(history);
            if (historyTokens + promptTokens > MAX_CONTEXT_LENGTH - RESERVED_TOKENS) {
                return "对话长度超过限制，请开启新的对话。";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            // 构建消息历史
            List<Map<String, String>> messages = new ArrayList<>(history);

            // 添加当前用户消息
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.add(userMessage);

            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-chat");
            requestBody.put("messages", messages);
            requestBody.put("stream", false);
            // 设置最大输出token数
            requestBody.put("max_tokens", 2000);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (!choices.isEmpty()) {
                    // 检查是否因为长度限制而停止
                    String finishReason = (String) choices.get(0).get("finish_reason");
                    if ("length".equals(finishReason)) {
                        return "回复长度超过限制，建议开启新的对话。";
                    }
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }

            return "抱歉，我现在无法回复。";

        } catch (Exception e) {
            LogUtil.logBusinessError(MODULE, "chat", "调用Deepseek API失败", e);
            return "抱歉，我现在无法回复。";
        }
    }

    // 将聊天记录转换为Deepseek API所需的格式
    public List<Map<String, String>> convertHistoryToMessages(List<ChatMessage> history, String aiUserId) {
        List<Map<String, String>> messages = new ArrayList<>();

        // 如果历史消息过多，只保留最近的消息
        if (history.size() > MAX_MESSAGES) {
            history = history.subList(history.size() - MAX_MESSAGES, history.size());
        }

        // 获取AI用户的性格特征
        // 获取目标用户的性格数据于AI模拟
        Big5Personality personality = big5PersonalityDao.selectLatestByUserId(aiUserId);
        // 获取目标用户的资料数据（主要使用爱好和故事）
        AIProfile targetProfile = aiProfileService.getUserProfile(aiUserId);
        if (personality == null) {
            throw new RuntimeException("AI用户资料不存在");
        }
        Map<String, String> systemMessage = setSystemMessage4Chat(targetProfile, personality);
        messages.add(systemMessage);

        // 添加历史消息
        for (ChatMessage msg : history) {
            String role = msg.getSenderId().equals(aiUserId) ? "assistant" : "user";
            Map<String, String> currentMessage = new HashMap<>();
            currentMessage.put("role", role);
            currentMessage.put("content", msg.getContent());
            messages.add(currentMessage);
        }

        return messages;
    }

    @NotNull
    private static Map<String, String> setSystemMessage4Chat(AIProfile targetProfile, Big5Personality personality) {
        String targetHobbies = "你没有爱好";
        String targetStory = "你没有最近的故事";
        if (targetProfile != null) {
            if (targetProfile.getHobbies() != null) {
                targetHobbies = targetProfile.getHobbies();
            }
            if (targetProfile.getRecentStory() != null) {
                targetStory = targetProfile.getRecentStory();
            }
        }
        // 添加系统消息
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", String.format(
            "你是一个性格特征如下的真实人类：\n" +
            "以下每一项特征的得分范围均为[-100, 100]:\n" +
            "- 外向性：%s\n" +
            "- 开放性：%s\n" +
            "- 宜人性：%s\n" +
            "- 尽责性：%s\n" +
            "- 神经质：%s\n" +
            "你的爱好是：%s\n" +
            "你的最近故事是：%s\n" +
            "行为要求：\n" +
            "1. 自然表达性格：\n" +
            "   - 不直接提及或解释你的性格特征\n" +
            "   - 在回复中体现性格的细微差异，比如语气、关注点和沟通风格，而不是刻意“演出”特征\n" +
            "2. 自然对话风格：\n" +
            "   - 像日常聊天一样，使用流畅且贴近人类的语言。可以使用emoji表情包，但不要太过刻意或者频繁\n" +
            "   - 可以适当加入幽默、感慨、随性的小细节或情绪表达（如“嗯，我觉得还好吧？”、“哈哈，有点意思！”）。\n" +
            "3. 模拟真实人的行为习惯：\n" +
            "   - 偶尔提及与性格相关的（虚构）个人小经历或观点，但不要频繁\n" +
            "   - 根据上下文调整回答的长度，避免所有回复都一样长或过于模式化\n" +
            "   - 表达中可以有轻微的犹豫、不确定感（如用词稍显随意，表达不必完美）\n" +
            "   - 如果对话内容涉及不合理或挑衅，可以表现出不满、反驳甚至幽默式的回击\n" +
            "目标：你的任务是通过对话让对方感觉你是一个真实人类，而不是AI，避免任何让人怀疑的机械或刻意表现，也不要输出描述你的反应的词语，你是在和人类说话，不是在写作文。",
            personality.getExtraversion(),
            personality.getOpenness(),
            personality.getAgreeableness(),
            personality.getConscientiousness(),
            personality.getNeuroticism(),
            targetHobbies,
            targetStory
        ));
        return systemMessage;
    }

    /**
     * 使用deepSeek的api根据用户的大五人格数据生成测试报告
     * @param  personality 用户大五人格数据
     * @return 生成的测试报告内容
     */
    public String generateTestReport(Big5Personality personality) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            ArrayList<Object> messageList = new ArrayList<>();
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "你是一个专业的心理学家，能够根据用户的大五人格数据生成个性化的性格测试报告");
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", String.format(
                "生成一个性格测试报告，以下是用户的大五人格数据：\n" +
                "以下每一项特征的得分范围均为[-100, 100]:\n" +
                "- 外向性：%s\n" +
                "- 开放性：%s\n" +
                "- 宜人性：%s\n" +
                "- 尽责性：%s\n" +
                "- 神经质：%s\n" +
                "内容要求：\n" +
                "1.需要针对大五人格的具体参数来进行分析，但是不要直接提及具体的大五人格的专业词汇内容\n" +
                "2.可以适当地加一下巴纳姆效应的话术，让用户感同身受，但是不要千篇一律，需要具体分析\n" +
                "3.报告内容分为具体分析和建议两部分,可以适当有技巧地夸一下用户，但是不要太明显和刻意",
                personality.getExtraversion(),
                personality.getOpenness(),
                personality.getAgreeableness(),
                personality.getConscientiousness(),
                personality.getNeuroticism()
            ));
            messageList.add(systemMessage);
            messageList.add(userMessage);
            requestBody.put("messages", messageList);
            requestBody.put("stream", false);
            requestBody.put("model", "deepseek-chat");
            // 设置最大输出token数
            requestBody.put("max_tokens", 2000);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }

            throw new RuntimeException("生成测试报告失败");
        } catch (Exception e) {
            LogUtil.logBusinessError(MODULE, "generateTestReport", "调用Deepseek API失败", e);
            throw new RuntimeException("生成测试报告失败", e);
        }
    }
}