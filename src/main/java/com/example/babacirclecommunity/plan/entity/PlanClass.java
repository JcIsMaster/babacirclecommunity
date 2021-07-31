package com.example.babacirclecommunity.plan.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/7/12 17:40
 */
@Data
public class PlanClass {

    private int id;

    /**
     * 计划id
     */
    private int planId;

    /**
     * 电商平台标签id
     */
    private int tagId;

    /**
     * 课程名
     */
    private String className;

    /**
     * 课程介绍
     */
    private String classDesc;

    /**
     * 课程海报
     */
    private String classPoster;

    /**
     * 日期权重
     */
    private Double dateWeight;

    /**
     * 是否为增强计划课程（0.普通  1.增强）
     */
    private int isEnhance;

    /**
     * 已学习人数
     */
    private int studentsNumber;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否有效
     */
    private int isDelete;
}
