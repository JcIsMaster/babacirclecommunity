package com.example.babacirclecommunity.talents.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/6/5 14:43
 */
@Data
public class TalentsVo {

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
     * 性别 0女 1男
     */
    private int sex;

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
     * 一句话介绍
     */
    private String introduction;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否展示(删除)
     */
    private int isDelete;

}
