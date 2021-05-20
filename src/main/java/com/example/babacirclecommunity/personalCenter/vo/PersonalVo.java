package com.example.babacirclecommunity.personalCenter.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/5/20 16:29
 */
@Data
public class PersonalVo {

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
     * 是否关注
     */
    private int whetherAttention;
}
