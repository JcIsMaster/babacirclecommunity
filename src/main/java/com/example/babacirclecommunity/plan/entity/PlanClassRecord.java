package com.example.babacirclecommunity.plan.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/7/22 10:37
 */
@Data
public class PlanClassRecord {

    private int id;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 课程id
     */
    private int planClassId;

    /**
     * 视频id
     */
    private int videoId;

    /**
     * 观看时长
     */
    private double watchTime;

    /**
     * 视频总时长
     */
    private double totalVideoDuration;

    /**
     * 视频观看进度
     */
    private int videoViewingProgress;

    /**
     * 距离上次观看的进度差值
     */
    private double differenceFromLastTime;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 是否有效
     */
    private int isDelete;

}
