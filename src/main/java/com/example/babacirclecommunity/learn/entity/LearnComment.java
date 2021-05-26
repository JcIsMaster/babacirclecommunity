package com.example.babacirclecommunity.learn.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/4/20 17:27
 */
@Data
public class LearnComment {

    private int id;

    /**
     * 评论人id
     */
    private int pId;

    /**
     * 被评论人id
     */
    private int bId;

    /**
     * 帖子id
     */
    private int tId;

    /**
     * 帖子类型(0:提问1:干货2:公开课)
     */
    private int tType;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论点赞状态（0没有，1点赞）
     */
    private int giveStatus;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否有效
     */
    private int isDelete;
}
