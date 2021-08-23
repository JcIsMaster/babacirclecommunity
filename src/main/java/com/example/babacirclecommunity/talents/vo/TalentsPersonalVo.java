package com.example.babacirclecommunity.talents.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/8/21 11:04
 */
@Data
public class TalentsPersonalVo {

    /**
     * id
     */
    private int id;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String userName;

    /**
     * 城市(省)
     */
    private String currProvince;

    /**
     * 城市(市)
     */
    private String city;

    /**
     * 职位
     */
    private String position;

    /**
     * 特长（擅长）
     */
    private String specialty;

    /**
     * 一句话介绍
     */
    private String introduction;

    /**
     * 图片作品
     */
    private String imgWorks;

    /**
     * 视频作品
     */
    private String videoWorks;
}
