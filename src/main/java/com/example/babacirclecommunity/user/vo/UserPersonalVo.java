package com.example.babacirclecommunity.user.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/8/19 9:35
 */
@Data
public class UserPersonalVo {

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
     * 用户所在市
     */
    private String city;

    /**
     * 用户性别
     */
    private int userSex;

    /**
     * 用户生日
     */
    private String birthday;

    /**
     * 用户年龄
     */
    private String age;

    /**
     * 介绍
     */
    private String resourceIntroduce;

}
