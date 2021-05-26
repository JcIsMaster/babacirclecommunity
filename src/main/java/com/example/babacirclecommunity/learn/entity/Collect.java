package com.example.babacirclecommunity.learn.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/4/20 15:52
 */
@Data
public class Collect {

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
     * 1收藏，0取消收藏
     */
    private int giveCancel;
}
