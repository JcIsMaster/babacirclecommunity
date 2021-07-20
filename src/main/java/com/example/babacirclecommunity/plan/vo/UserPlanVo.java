package com.example.babacirclecommunity.plan.vo;

import com.example.babacirclecommunity.plan.entity.PlanClass;
import lombok.Data;

import java.util.List;

/**
 * @author JC
 * @date 2021/7/12 16:50
 */
@Data
public class UserPlanVo {

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
     * 课程开始时间到今天+2的课程列表
     */
    private List<PlanClass> todayCourse;

    /**
     * 创建时间
     */
    private String createAt;
}
