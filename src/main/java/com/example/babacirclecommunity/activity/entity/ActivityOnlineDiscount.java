package com.example.babacirclecommunity.activity.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author JC
 * @date 2021/10/13 17:54
 */
@Data
public class ActivityOnlineDiscount {

    private int id;

    /**
     * 线上活动参与表id
     */
    private int onlineParticipateId;

    /**
     * 帮砍价人id
     */
    private int userId;

    /**
     * 砍价幅度
     */
    private BigDecimal discountRate;

    /**
     * 创建时间
     */
    private String createAt;
}
