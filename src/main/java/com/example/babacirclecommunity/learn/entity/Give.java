package com.example.babacirclecommunity.learn.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/4/20 11:36
 */
@Data
public class Give {

    private int id;

    /**
     * 帖子id
     */
    private int zqId;

    /**
     * 帖子类型
     */
    private int learnType;

    /**
     * 用户id
     */
    private int uId;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 1点赞，0取消点赞
     */
    private int giveCancel;
}
