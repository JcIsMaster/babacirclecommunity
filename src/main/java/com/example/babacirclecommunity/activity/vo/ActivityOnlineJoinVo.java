package com.example.babacirclecommunity.activity.vo;

import com.example.babacirclecommunity.activity.entity.ActivityOnlineDiscount;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author JC
 * @date 2021/10/13 17:45
 * 发起我的砍价进入详情页面
 */
@Data
public class ActivityOnlineJoinVo {

    /**
     * 活动id
     */
    private int id;

    /**
     * 活动参与id
     */
    private int participateId;

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
     * 活动发起人名称
     */
    private String initiatorUserName;

    /**
     * 活动发起人名称
     */
    private String initiatorAvatar;

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
     * 参与时间
     */
    private String participateTime;

    /**
     * 店铺名
     */
    private String shopName;

    /**
     * 砍价记录
     */
    private List<ActivityOnlineDiscountVo> activityOnlineDiscounts;

    /**
     * 总优惠
     */
    private BigDecimal totalDiscount;

}
