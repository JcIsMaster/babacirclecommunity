package com.example.babacirclecommunity.learn.vo;

import lombok.Data;

import java.util.List;

/**
 * @author JC
 * @date 2021/4/20 17:59
 */
@Data
public class LearnCommentReplyVo {

    private int id;

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
     * 一级评论点赞数量
     */
    private int commentGiveNum;

    /**
     * 一级评论是否点赞
     */
    private int commentGiveStatus=0;


    /**
     * 用户id
     */
    private int userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 二级评论集合
     */
    List<LearnPostReplyVo> postReplyList;

    /**
     * 二级评论数量
     */
    private int commentSize;
}
