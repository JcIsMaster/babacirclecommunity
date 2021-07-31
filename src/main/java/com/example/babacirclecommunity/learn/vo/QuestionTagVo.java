package com.example.babacirclecommunity.learn.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/4/29 14:24
 */
@Data
public class QuestionTagVo {

    private int id;
    /**
     * 提问人id
     */
    private int userId;
    /**
     * 发问人名称
     */
    private String userName;
    /**
     * 发问人头像
     */
    private String avatar;
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
     * 发布时间
     */
    private String createAt;
    /**
     * 提问tagsTwo对应的tagName
     */
    private String tagName;
    /**
     * 看贴人对该帖子的点赞状态 0:未点赞； 1:已点赞
     */
    private int whetherGive;
}
