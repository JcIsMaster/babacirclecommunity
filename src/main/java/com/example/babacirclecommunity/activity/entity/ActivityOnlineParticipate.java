package com.example.babacirclecommunity.activity.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/10/13 16:39
 */
@Data
public class ActivityOnlineParticipate {

    private int id;

    /**
     * 活动参与人id
     */
    private int userId;

    /**
     * 线上活动id
     */
    private int activityOnlineId;

    /**
     * 参与时间
     */
    private String createAt;

    /**
     * 状态 0.进行中  1.已终止
     */
    private int isStatus;
}
