package com.example.babacirclecommunity.plan.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/7/12 16:19
 */
@Data
public class UserPlan {

    private int id;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 计划id
     */
    private int planId;

    /**
     * 签到日期记录（json字符串）
     */
    private String singInRecord;

    /**
     * 完成进度（-1为计划课程全部完成）
     */
    private int completeSchedule;

    /**
     * 创建时间
     */
    private String createAt;
}
