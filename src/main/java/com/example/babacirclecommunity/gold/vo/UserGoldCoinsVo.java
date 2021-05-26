package com.example.babacirclecommunity.gold.vo;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/4/19 16:34
 */
@Data
public class UserGoldCoinsVo {

    /**
     * 连续签到天数
     */
    private int consecutiveNumber;


    /**
     * 是否可以签到 0不可以 1可以
     */
    private int whetherCanCheckIn;
}
