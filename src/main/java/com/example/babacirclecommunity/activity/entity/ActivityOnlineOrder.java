package com.example.babacirclecommunity.activity.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/10/16 16:49
 */
@Data
public class ActivityOnlineOrder {

    private int id;
    /**
     * 活动id
     */
    private int activityOnlineId;
    /**
     * 购买人id
     */
    private int userId;
    /**
     * 购买价格
     */
    private int price;
    /**
     * 创建时间
     */
    private String createAt;
    /**
     * 0:有效；1:无效； 默认0
     */
    private int isDelete;
}
