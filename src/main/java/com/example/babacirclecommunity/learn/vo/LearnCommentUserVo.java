package com.example.babacirclecommunity.learn.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/4/20 17:58
 */
@Data
public class LearnCommentUserVo {

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
    private int giveNum;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 用户名称
     */
    private String userName;
}
