package com.example.babacirclecommunity.learn.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/7/31 16:26
 */
@Data
public class LearnCommentVo {

    private int id;

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
     * 评论内容
     */
    private String commentContent;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 评论点赞数量
     */
    private int commentGiveNum;

    /**
     * 评论是否点赞
     */
    private int commentGiveStatus = 0;
}
