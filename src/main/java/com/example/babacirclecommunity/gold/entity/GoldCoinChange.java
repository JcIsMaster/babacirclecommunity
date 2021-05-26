package com.example.babacirclecommunity.gold.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/4/20 17:08
 */
@Data
public class GoldCoinChange {

    private int id;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 金币获取来源方法
     */
    private String sourceGoldCoin;

    /**
     * 金币正负
     */
    private String positiveNegativeGoldCoins;

    /**
     * 创建时间
     */
    private String createAt;
}
