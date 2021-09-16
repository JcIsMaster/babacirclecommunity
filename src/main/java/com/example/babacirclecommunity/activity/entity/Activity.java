package com.example.babacirclecommunity.activity.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/9/15 15:03
 */
@Data
public class Activity {

    /**
     * id
     */
    private int id;

    /**
     * 活动标题
     */
    private String activityTitle;

    /**
     * 活动封面
     */
    private String activityCover;

    /**
     * 活动内容
     */
    private String activityContent;

    /**
     * 活动主办方或承办单位
     */
    private String activitySponsor;

    /**
     * 活动开始-活动结束时间
     */
    private String activityTime;

    /**
     * 活动地点
     */
    private String activityLocation;

    /**
     * 活动发起人id
     */
    private int sponsorUserId;

    /**
     * 活动费用
     */
    private int activityFee;

    /**
     * 活动费用用途
     */
    private String activityFeeDesc;

    /**
     * 活动规则
     */
    private String activityRule;

    /**
     * 活动开始时间
     */
    private String activityStartTime;

    /**
     * 活动结束时间
     */
    private String activityEndTime;

    /**
     * 活动是否有效 0.有效  1.过期
     */
    private int isDelete;

    /**
     * 活动是否已参与 0.未参与  1.已参与
     */
    private int participationStatus = 0;

}
