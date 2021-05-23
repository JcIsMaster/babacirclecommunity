package com.example.babacirclecommunity.CircleFriends.vo;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/5/23 14:16
 */
@Data
public class CircleFriendsVo {

    /**
     * 分享类型 0圈子
     */
    private int type;

    /**
     * 帖子id
     */
    private int id;

    /**
     * 用户头像
     */
    private String headUrl;

    /**
     * 帖子第一张图片
     */
    private String postImg;

    /**
     * 内容
     */
    private String postContent;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 二维码指向的地址
     */
    private String pageUrl;

    /**
     * 标题
     */
    private String title;
}
