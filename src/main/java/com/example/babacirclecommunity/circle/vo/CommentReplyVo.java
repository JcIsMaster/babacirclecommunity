package com.example.babacirclecommunity.circle.vo;

import lombok.Data;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/4 15:13
 */
@Data
public class CommentReplyVo {

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
    List<PostReplyVo> postReplyList;

    /**
     * 二级评论数量
     */
    private int commentSize;
}
