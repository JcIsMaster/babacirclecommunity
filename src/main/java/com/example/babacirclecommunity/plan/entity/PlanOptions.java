package com.example.babacirclecommunity.plan.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/7/12 10:15
 */
@Data
public class PlanOptions {

    /**
     * 唯一标识 主键
     */
    private int id;

    /**
     * 选项
     */
    private String options;

    /**
     * 关联的题目id
     */
    private int topicId;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否有效（0 有效  1 无效）
     */
    private int isDelete;
}
