package com.example.babacirclecommunity.learn.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/4/28 13:55
 */
@Data
public class Question {

    private int id;
    /**
     * 提问人id
     */
    private int userId;
    /**
     * 标题
     */
    private String title;
    /**
     * 二级标签id
     */
    private int tagsTwo;
    /**
     * 单元体类型id
     */
    private int haplontId;
    /**
     * 计划课程id 0为未关联课程
     */
    private int planClassId;
    /**
     * 点赞数量
     */
    private int favourNum;
    /**
     * 评论数量
     */
    private int commentNum;
    /**
     * 描述
     */
    private String description;
    /**
     * 删除状态0:有效；1:无效； 默认0
     */
    private int isDelete;
    /**
     * 发布时间
     */
    private String createAt;
}
