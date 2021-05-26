package com.example.babacirclecommunity.learn.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/4/21 9:18
 */
@Data
public class LearnPostReplyVo {

    /**
     * 二级评论id
     */
    private int id;


    /**
     * 回复人id
     */
    private int userId;

    /**
     * 回复人名称
     */
    private String userName;

    /**
     * 回复人头像
     */
    private String avatar;

    /**
     *评论内容
     */
    private String hContent;

    /**
     * 二级评论点赞数量
     */
    private int twoCommentGiveNum;

    /**
     * 二级评论是否点赞
     */
    private int twoCommentGiveStatus=0;

    /**
     * 评论点赞状态（0没有，1点赞）
     */
    private int replyGiveStatus;

    /**
     * 被回复人id
     */
    private int bhId;

    /**
     * 被回复人名称
     */
    private String uName;
}
