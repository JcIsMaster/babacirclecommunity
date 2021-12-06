package com.example.babacirclecommunity.activity.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author JC
 * @date 2021/10/6 11:43
 */
@Data
public class ActivityOnlineListVo {

    private int id;

    /**
     * 封面
     */
    private String cover;

    /**
     * 标题
     */
    private String title;

    /**
     * 活动发起人id
     */
    private int initiatorUserId;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 折扣价
     */
    private BigDecimal discountPrice;
}
