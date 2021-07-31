package com.example.babacirclecommunity.learn.vo;

import lombok.Data;

import java.util.List;

/**
 * @author JC
 * @date 2021/4/28 13:59
 */
@Data
public class QuestionVo {

    private int id;
    /**
     * 提问人id
     */
    private int userId;
    /**
     * 提问人名称
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
     * 描述
     */
    private String description;
    /**
     * 点赞数量
     */
    private int favourNum;
    /**
     * 评论数量
     */
    private int commentNum;
    /**
     * 发布时间
     */
    private String createAt;
    /**
     * 看贴人对该帖子的点赞状态 0:未点赞； 1:已点赞
     */
    private int whetherGive;
}
