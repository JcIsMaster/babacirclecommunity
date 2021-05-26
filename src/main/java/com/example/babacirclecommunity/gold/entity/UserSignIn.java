package com.example.babacirclecommunity.gold.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/4/13 14:25
 * 金币实体类
 */
@Data
public class UserSignIn {

    private int id;

    /**
     * 金币数量
     */
    private int goldNumber;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 签到状态 0未签到 1已签到
     */
    private int type;

    /**
     * 创建时间
     */
    private String createAt;
}
