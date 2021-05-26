package com.example.babacirclecommunity.gold.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/4/13 14:27
 * 用户金币表
 */
@Data
public class UserGoldCoins {

    private int id;

    /**
     * 可提现金币
     */
    private int canWithdrawGoldCoins;

    /**
     * 不可提现金币
     */
    private int mayNotWithdrawGoldCoins;

    /**
     * 签到得到的金币数量
     */
    private int signInGetGoldCoins;

    /**
     * 连续签到天数
     */
    private int consecutiveNumber;

    /**
     * 上一次签到时间
     */
    private String lastCheckinTime;

    /**
     * 用户id
     */
    private String openId;


}
