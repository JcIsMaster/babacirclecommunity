package com.example.babacirclecommunity.activity.service;

import com.example.babacirclecommunity.activity.entity.Activity;
import com.example.babacirclecommunity.activity.entity.ActivityParticipate;
import com.example.babacirclecommunity.activity.vo.ActivityListVo;
import com.example.babacirclecommunity.common.utils.Paging;

import java.text.ParseException;
import java.util.List;

/**
 * @author JC
 * @date 2021/9/15 15:08
 */
public interface IActivityService {

    /**
     * 查询活动列表
     * @param paging
     * @return
     */
    List<ActivityListVo> queryActivityList(Paging paging);

    /**
     * 查询活动详情
     * @param id
     * @param userId
     * @return
     */
    Activity queryActivityDetailsById(int id, int userId);

    /**
     * 用户参与活动
     * @param activityParticipate
     * @return
     */
    int participateInActivity(ActivityParticipate activityParticipate);

    /**
     * 创建活动
     * @param activity
     * @exception ParseException
     * @return
     */
    int createActivity(Activity activity) throws ParseException;
}
