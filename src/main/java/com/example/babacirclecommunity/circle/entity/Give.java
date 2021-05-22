package com.example.babacirclecommunity.circle.entity;

import lombok.Data;

/**
 * @author MQ
 * @create 2021/2/22
 * 点赞 实体类
 **/
@Data
public class Give {

    private int id;

    /**
     * 帖子id
     */
    private int zqId;

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
