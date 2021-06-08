package com.example.babacirclecommunity.my.vo;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/6/8 14:14
 * 我关注的人
 */
@Data
public class PeopleCareAboutVo {

    /**
     * 用户id
     */
    private int id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户介绍
     */
    private String introduce;
}
