package com.example.babacirclecommunity.plan.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/7/17 10:59
 */
@Data
public class PlanClassFeedback {

    private int id;

    /**
     * 计划课程id
     */
    private int planClassId;

    /**
     * 打分
     */
    private Double scoring;

    /**
     * 标签
     */
    private String feedbackTag;

    /**
     * 反馈内容
     */
    private String detail;

    /**
     * 创建时间
     */
    private String createAt;

}
