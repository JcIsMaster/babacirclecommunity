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
     * 用户性别
     */
    private int userSex;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 用户年龄
     */
    private String age;

    /**
     * 所在省
     */
    private String currProvince;

    /**
     * 所在市
     */
    private String city;

    /**
     * 所在县
     */
    private String county;

    /**
     * 用户粉丝数
     */
    private int fansNum;

    /**
     * 用户关注数
     */
    private int attentionNum;


}
