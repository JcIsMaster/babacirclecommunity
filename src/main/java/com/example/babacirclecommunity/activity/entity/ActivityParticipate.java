package com.example.babacirclecommunity.activity.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/9/15 16:54
 */
@Data
public class ActivityParticipate {

    /**
     * id
     */
    private int id;

    /**
     * 活动id
     */
    private int activityId;

    /**
     * 活动参与人id
     */
    private int userId;

    /**
     * 活动参与人姓名
     */
    private String userName;

    /**
     * 活动参与人电话
     */
    private String userPhoneNumber;

    /**
     * 活动参与人地址
     */
    private String userAddress;

    /**
     * 参与活动时间
     */
    private String createAt;

    /**
     * 是否有效 0.有效  1.无效
     */
    private int isDelete;
}
