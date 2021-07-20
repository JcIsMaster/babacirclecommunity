package com.example.babacirclecommunity.plan.service.impl;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.plan.dao.MakingPlanMapper;
import com.example.babacirclecommunity.plan.entity.*;
import com.example.babacirclecommunity.plan.service.IMakingPlanService;
import com.example.babacirclecommunity.plan.vo.PlanClassVo;
import com.example.babacirclecommunity.plan.vo.TopicOptionsVo;
import com.example.babacirclecommunity.plan.vo.UserPlanVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JC
 * @date 2021/7/12 13:40
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class MakingPlanServiceImpl implements IMakingPlanService {

    @Autowired
    private MakingPlanMapper makingPlanMapper;


    @Override
    public List<TopicOptionsVo> getTopicAndOptions() {
        List<TopicOptionsVo> topicOptionsVos = makingPlanMapper.getPlanTopic();
        List<PlanOptions> planOptions = makingPlanMapper.getPlanOptions();
        for (TopicOptionsVo topicOptionsVo : topicOptionsVos) {
            topicOptionsVo.setPlanOptionsList(planOptions.stream().filter(s -> s.getTopicId() == topicOptionsVo.getId()).collect(Collectors.toList()));
        }
        return topicOptionsVos;
    }

    @Override
    public UserPlanVo queryUserPlan(int userId) {
        UserPlanVo userPlanVo = makingPlanMapper.queryUserPlan(userId);
        if(userPlanVo == null){
            return null;
        }
        userPlanVo.setTodayCourse(makingPlanMapper.queryClassLearnList(userPlanVo.getPlanId(),userPlanVo.getCompleteSchedule()));
        return userPlanVo;
    }

    @Override
    public UserPlanVo generatePlan(int userId,String planOptions) {
        //根据目标参数找到需要的计划id和计划的开始时间
        Plan plan = makingPlanMapper.queryPlanByOptions(planOptions);
        if(plan == null){
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND);
        }
        //添加用户计划数据
        UserPlan userPlan = new UserPlan();
        userPlan.setUserId(userId);
        userPlan.setPlanId(plan.getId());
        userPlan.setCreateAt(System.currentTimeMillis() / 1000 + "");
        int i = makingPlanMapper.generatePlan(userPlan);
        if (i <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        //返回用户计划详情（签到记录以及课程学习列表）
        UserPlanVo userPlanVo = new UserPlanVo();
        userPlanVo.setPlanId(userPlan.getPlanId());
        userPlanVo.setUserId(userId);
        userPlanVo.setTodayCourse(makingPlanMapper.queryClassLearnList(userPlan.getPlanId(),0));

        return userPlanVo;
    }

    @Override
    public PlanClassVo queryPlanClassDetail(int planClassId) {
        PlanClassVo planClassVo = makingPlanMapper.queryPlanClassDetail(planClassId);
        planClassVo.setClassVideos(makingPlanMapper.queryPlanClassVideoList(planClassId));
        return planClassVo;
    }

    @Override
    public int addPlanFeedBack(PlanClassFeedback planClassFeedback) {
        int i = makingPlanMapper.addPlanFeedBack(planClassFeedback);
        if (i <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return i;
    }

    @Override
    public List<PlanClass> queryEnhancePlanListByTag(int tagId, Paging paging) {
        Integer page = (paging.getPage() - 1) * paging.getLimit();
        String sql = "limit " + page + "," + paging.getLimit() + "";

        return makingPlanMapper.queryEnhancePlanListByTag(tagId,sql);
    }
}
