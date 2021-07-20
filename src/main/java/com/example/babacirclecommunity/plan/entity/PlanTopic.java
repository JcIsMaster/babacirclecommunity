package com.example.babacirclecommunity.plan.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/7/12 10:12
 */
@Data
public class PlanTopic {

    /**
     * 唯一标识 主键
     */
    private int id;

    /**
     * 题目
     */
    private String topic;

    /**
     * 题目备注（小字）
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否有效（0 有效  1 无效）
     */
    private int isDelete;
}
