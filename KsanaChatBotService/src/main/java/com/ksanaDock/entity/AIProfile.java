package com.ksanaDock.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("ai_profile")
public class AIProfile {
    @TableId(type = IdType.AUTO)
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Size(max = 64, message = "用户ID长度不能超过64")
    private String userId;

    @NotNull(message = "用户昵称不能为空")
    @Size(max = 64, message = "用户昵称长度不能超过64")
    private String nickname;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime nicknameUpdateTime;

    @Size(max = 255, message = "头像URL长度不能超过255")
    private String avatar;

    @Pattern(regexp = "^(male|female|other)$", message = "性别只能是 male、female 或 other")
    private String gender;

    @Min(value = 0, message = "年龄不能小于0")
    @Max(value = 150, message = "年龄不能大于150")
    private Integer age;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Size(max = 100, message = "学校名称长度不能超过100")
    private String school;

    @Size(max = 500, message = "兴趣爱好长度不能超过500")
    private String hobbies;

    @Size(max = 2000, message = "最近的故事长度不能超过2000")
    private String recentStory;

    @Size(max = 10, message = "MBTI类型长度不能超过10")
    private String mbti;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
} 