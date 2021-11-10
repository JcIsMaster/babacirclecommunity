package com.example.babacirclecommunity.activity.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author JC
 * @date 2021/10/29 16:58
 */
@Data
public class ActivityOnlineOrderVo {

    /**
     * 线上活动订单表id
     */
    private int id;

    /**
     * 线上活动id
     */
    private int activityOnlineId;

    /**
     * 线上活动标题
     */
    private String title;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 折扣价
     */
    private BigDecimal discountPrice;

    /**
     * 状态 0.进行中  1.已结束
     */
    private int isStatus;

    /**
     * 购买人id
     */
    private int userId;

    /**
     * 购买人名字
     */
    private String userName;

    /**
     * 购买人头像
     */
    private String avatar;

    /**
     * 购买人实付款
     */
    private BigDecimal price;

    /**
     * 购买时间
     */
    private String createAt;
}
