package com.example.babacirclecommunity.gold.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/4/13 14:29
 * 帖子打赏实体类
 */
@Data
public class PostExceptional {

    private int id;

    /**
     * 帖子id
     */
    private int tId;

    /**
     * 打赏金币数量
     */
    private int amountGoldCoins;

    /**
     * 打赏人id
     */
    private int uId;

    /**
     * 创建时间
     */
    private String createAt;

}
