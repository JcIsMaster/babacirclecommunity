package com.example.babacirclecommunity.gold.vo;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/6/28 16:45
 */
@Data
public class GoldTimeVo {

    /**
     * 签到得到的金币数量
     */
    private int positiveNegativeGoldCoins;

    /**
     * 签到时间
     */
    private String createAt;

    /**
     * 金币获取与支出方式
     */
    private String sourceGoldCoin;
}
