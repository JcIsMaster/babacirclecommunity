package com.example.babacirclecommunity.circle.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/2/27 15:44
 * 评论实体类
 */
@Data
public class Comment {


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
