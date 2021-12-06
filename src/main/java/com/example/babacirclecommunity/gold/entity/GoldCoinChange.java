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
    private int positiveNegativeGoldCoins;

    /**
     * 0 充值，1签到，2干货 3公开课 4.在线活动 5.提现
     */
    private int sourceGoldCoinType;

    /***
     * 0支出 1收入
     */
    private int expenditureOrIncome;

    /**
     * 创建时间
     */
    private String createAt;
}
