package com.example.babacirclecommunity.user.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/5/21 11:37
 */
@Data
public class PersonalUserVo {

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
     * 是否关注 0未关注 1已关注
     */
    private int whetherAttention;
}
