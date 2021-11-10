package com.example.babacirclecommunity.activity.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author JC
 * @date 2021/10/16 11:03
 */
@Data
public class ActivityOnlineHelpInfoVo {

    /**
     * 线上活动参与表id
     */
    private int id;

    /**
     * 线上活动id
     */
    private int activityId;

    /**
     * 封面
     */
    private String cover;

    /**
     * 标题
     */
    private String title;

    /**
     * 活动参与人（发起砍价人）id
     */
    private int userId;

    /**
     * 活动参与人（发起砍价人）名称
     */
    private String userName;

    /**
     * 活动参与人（发起砍价人）头像
     */
    private String avatar;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 折扣价
     */
    private BigDecimal discountPrice;

    /**
     * 单次折扣率
     */
    private int singleDiscountRate;

    /**
     * 活动参与人发起砍价的时间
     */
    private String createAt;

    /**
     * 砍价记录
     */
    private List<ActivityOnlineDiscountVo> activityOnlineDiscounts;

    /**
     * 总优惠
     */
    private BigDecimal totalDiscount;
}
