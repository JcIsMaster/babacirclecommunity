package com.example.babacirclecommunity.circle.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/6 14:58
 */
@Data
public class CommentGive {

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
