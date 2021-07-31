package com.example.babacirclecommunity.plan.service;

import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.plan.entity.*;
import com.example.babacirclecommunity.plan.vo.PlanClassVo;
import com.example.babacirclecommunity.plan.vo.RecentlyPlanClassListVo;
import com.example.babacirclecommunity.plan.vo.TopicOptionsVo;
import com.example.babacirclecommunity.plan.vo.UserPlanVo;

import java.util.List;

/**
 * @author JC
 * @date 2021/7/12 11:59
 */
public interface IMakingPlanService {

    /**
     * 查询目标参数题目和选项
     * @return
     */
    List<TopicOptionsVo> getTopicAndOptions();

    /**
     * 查看用户计划
     * @param userId
     * @return
     */
    UserPlanVo queryUserPlan(int userId);

    /**
     * 生成用户计划
     * @param userId
     * @param planOptions
     * @return
     */
    UserPlanVo generatePlan(int userId,String planOptions);

    /**
     * 查询计划课程详情
     * @param planClassId 计划课程id
     * @return
     */
    PlanClassVo queryPlanClassDetail(int planClassId);

    /**
     * 新增计划课程反馈
     * @param planClassFeedback
     * @return
     */
    int addPlanFeedBack(PlanClassFeedback planClassFeedback);

    /**
     * 根据tagId查询增强计划列表
     * @param tagId
     * @param paging
     * @return
     */
    List<PlanClass> queryEnhancePlanListByTag(int tagId, Paging paging);

    /**
     * 添加课程学习记录
     * @param planClassRecord
     * @return
     */
    ResultUtil addLearningRecord(PlanClassRecord planClassRecord);

    /**
     * 查询我的学习
     * @param userId
     * @return
     */
    Object queryMyStudies(int userId);

    /**
     * 根据用户id查询该用户最近学习的课程列表
     * @param userId
     * @param paging
     * @return
     */
    List<PlanClass> queryRecentlyLearnedClassList(int userId, Paging paging);

    /**
     * 课程打卡签到
     * @param userId
     * @param planId
     * @return
     */
    ResultUtil planClassSingIn(int userId, int planId);
}
