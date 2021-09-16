package com.example.babacirclecommunity.activity.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/9/15 15:15
 */
@Data
public class ActivityListVo {

    /**
     * 活动id
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
     * 活动发起人id
     */
    private int sponsorUserId;

    /**
     * 活动发起人名字
     */
    private String sponsorUserName;

    /**
     * 活动发起人头像
     */
    private String sponsorUserAvatar;

    /**
     * 活动结束时间
     */
    private String activityEndTime;

    /**
     * 活动参与人数
     */
    private int numberOfParticipants;
}
