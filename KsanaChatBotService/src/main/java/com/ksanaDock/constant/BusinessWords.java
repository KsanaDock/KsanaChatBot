package com.ksanaDock.constant;

/**
 * 业务常量
 */
public interface BusinessWords {
    String USER = "USER";
    String AI = "AI";

    // 昵称修改规则
    String NICKNAME_LENGTH_RULE = "昵称长度必须在2-10个字符之间";
    String NICKNAME_CHARACTER_RULE = "昵称只能包含中文、英文字母和数字";
    String NICKNAME_EXIST_RULE = "昵称已被占用";
    String NICKNAME_UPDATE_RULE = "昵称30天内只能修改一次";

    // 帮助反馈
    String FEEDBACK_TOO_MANY ="您有太多未处理的反馈，请等待处理完成后再提交";
}
