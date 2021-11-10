package com.example.babacirclecommunity.user.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/11/2 14:17
 */
@Data
public class UserRankVo {

    /**
     * 用户id
     */
    private int id;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 帖子点赞总量
     */
    private int giveNumber;

    /**
     * 帖子浏览总量
     */
    private int browse;

}
