package com.example.babacirclecommunity.user.vo;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/5/20 19:49
 */
@Data
public class PersonalCenterUserVo {

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
     * 介绍
     */
    private String introduce;

    /**
     * 背景图
     */
    private String picture;


}
