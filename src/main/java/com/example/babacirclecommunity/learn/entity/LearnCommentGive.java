package com.example.babacirclecommunity.learn.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/4/20 17:30
 */
@Data
public class LearnCommentGive {

    private int id;

    /**
     * 评论id
     */
    private int commentId;

    /**
     * 点赞人id
     */
    private int dId;

    /**
     *被点赞人id
     */
    private int bdId;

    /**
     *创建时间
     */
    private String createAt;

    /**
     *0一级评论 ，1二级评论
     */
    private int type;

    /**
     *评论点赞状态（0没有，1点赞）
     */
    private int giveStatus;

    /**
     *是否有效
     */
    private int isDelete;
}
