package com.ksanaDock.entity;

import java.util.Date;

/**
 * 大五人格实体类
 */
public class Big5Personality {

    /**
     * 大五人格id
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 开放性得分[-100, 100]
     * 这一维度主要涉及个体对新经验的开放程度。开放性高的人富有想象力和创造力，他们对艺术、文化等各种新鲜事物充满兴趣，乐于尝试新的观念和活动。例如，他们可能会热衷于参观现代艺术展览、尝试异国美食或者学习一门全新的语言。
     * 相反，开放性较低的人则比较传统和保守，更倾向于熟悉的事物和常规的做法。他们可能对抽象艺术不太理解，更偏爱传统的美食，对于新的观念也会持比较谨慎的态度。
     */
    private String openness;

    /**
     * 尽责性得分[-100, 100]
     * 体现了个体的自律性、责任感和条理性。尽责性高的人做事认真负责、有条理，有较强的自我约束能力，能够很好地规划自己的时间和任务。比如，在工作中，他们会严格遵守工作时间表，认真完成工作任务，并且注重细节，很少会出现粗心大意的情况。
     * 尽责性较低的人可能会比较随意，有时会缺乏计划性，容易拖延，对任务的完成质量要求也相对不高。
     */
    private String conscientiousness;

    /**
     * 外向性得分[-100, 100]
     * 主要描述个体在社交情境中的倾向。外向性高的人喜欢社交活动，充满活力，在人群中往往能够如鱼得水，善于表达自己的想法和情感。他们享受成为焦点的感觉，例如在聚会中，他们会积极主动地与人交谈、参与各种游戏，并且能够从与他人的互动中获得能量。
     * 而外向性低的人则更倾向于独处，他们在社交场合可能会感到疲惫，比较安静、内敛，通常会在小范围内和熟悉的人交往，更注重内心的思考和体验。
     */
    private String extraversion;

    /**
     * 宜人性得分[-100, 100]
     * 反映个体在人际交往中的友善、合作和同情心等品质。宜人性高的人富有同情心，善于理解他人，乐于助人，并且愿意与他人合作，很少会与人发生冲突。在团队合作中，他们能够很好地协调成员之间的关系，关注他人的感受。
     * 宜人性较低的人可能会比较自我中心，对他人的需求不太敏感，在竞争或冲突情境中可能会更注重自己的利益。
     */
    private String agreeableness;

    /**
     * 神经质得分[-100, 100]
     * 衡量个体情绪的稳定性。神经质得分高的人情绪容易波动，可能会经常体验到焦虑、抑郁、愤怒等负面情绪，并且对压力比较敏感。例如，在面对工作压力或者生活挫折时，他们可能会陷入长时间的情绪低落或者焦虑不安之中。
     * 神经质得分低的人情绪相对稳定，能够更好地应对压力和挫折，保持冷静和乐观的心态。
     */
    private String neuroticism;

    /**
     * 测试报告
     */
    private String testResultReport;

    public String getTestResultReport() {
        return testResultReport;
    }

    public void setTestResultReport(String testResultReport) {
        this.testResultReport = testResultReport;
    }

    private Date createTime;

    private Date updateTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Big5Personality(String neuroticism, String openness, String conscientiousness, String extraversion, String agreeableness, String userId, String id) {
        this.neuroticism = neuroticism;
        this.openness = openness;
        this.conscientiousness = conscientiousness;
        this.extraversion = extraversion;
        this.agreeableness = agreeableness;
        this.userId = userId;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpenness() {
        return openness;
    }

    public void setOpenness(String openness) {
        this.openness = openness;
    }

    public String getConscientiousness() {
        return conscientiousness;
    }

    public void setConscientiousness(String conscientiousness) {
        this.conscientiousness = conscientiousness;
    }

    public String getExtraversion() {
        return extraversion;
    }

    public void setExtraversion(String extraversion) {
        this.extraversion = extraversion;
    }

    public String getAgreeableness() {
        return agreeableness;
    }

    public void setAgreeableness(String agreeableness) {
        this.agreeableness = agreeableness;
    }

    public String getNeuroticism() {
        return neuroticism;
    }

    public void setNeuroticism(String neuroticism) {
        this.neuroticism = neuroticism;
    }
}
