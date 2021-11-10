package com.example.babacirclecommunity.activity.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author JC
 * @date 2021/10/6 11:25
 */
@Data
public class ActivityOnline {

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

    /**
     * 库存
     */
    private int stock;

    /**
     * 结束时间
     */
    private String finishTime;

    /**
     * 店铺名
     */
    private String shopName;

    /**
     * 店铺网址
     */
    private String shopUrl;

    /**
     * 商家电话
     */
    private String shopPhone;

    /**
     * 微信号
     */
    private String weChatNumber;

    /**
     * 规则描述
     */
    private String description;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 浏览量
     */
    private int browse;

    /**
     * 转发量
     */
    private int share;

    /**
     * 单次折扣率
     */
    private int singleDiscountRate;

    /**
     * 状态 0.进行中  1.已结束
     */
    private int isStatus;
}
