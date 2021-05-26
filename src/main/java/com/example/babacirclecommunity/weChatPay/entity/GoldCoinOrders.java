package com.example.babacirclecommunity.weChatPay.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author MQ
 * @date 2021/4/17 14:32
 */
@Data
public class GoldCoinOrders {

    private int id;

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 商品类型  0 金币
     */
    private int goodsNo;

    /**
     * 订单状态 0待支付，1已支付 , 2已取消
     */
    private int orderStatus;

    /**
     * 下单人用户id
     */
    private int userId;

    /**
     * 订单金额
     */
    private BigDecimal money;

    /**
     * 创建时间
     */
    private String createAt;
}
