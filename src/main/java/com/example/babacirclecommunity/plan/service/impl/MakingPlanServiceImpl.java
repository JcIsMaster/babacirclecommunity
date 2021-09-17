package com.example.babacirclecommunity.plan.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.common.utils.TimeUtil;
import com.example.babacirclecommunity.gold.vo.SingInVo;
import com.example.babacirclecommunity.plan.dao.MakingPlanMapper;
import com.example.babacirclecommunity.plan.entity.*;
import com.example.babacirclecommunity.plan.service.IMakingPlanService;
import com.example.babacirclecommunity.plan.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
        //今日课程及继续播放进度
        PlanClassTodayVo planClassTodayVo = null;
        if (userPlanVo.getCompleteSchedule() == -1){
            //计划课程一全部完成返回空
            return null;
        }
        if (userPlanVo.getCompleteSchedule() != 1){
            planClassTodayVo = makingPlanMapper.queryTodayClass(userId);
        }
        else {
            planClassTodayVo = makingPlanMapper.queryTodayClassOne(userId);
        }
        PlanClassRecord classRecord = makingPlanMapper.queryTodayClassRecord(userId, planClassTodayVo.getId());
        if (classRecord != null){
            planClassTodayVo.setRecentlyPlayedVideoId(classRecord.getVideoId());
            planClassTodayVo.setRecentlyPlayedVideoTime(classRecord.getWatchTime());
            planClassTodayVo.setRecentlyPlayedVideoProgress(classRecord.getVideoViewingProgress());
        }
        else {
            planClassTodayVo.setRecentlyPlayedVideoId(0);
            planClassTodayVo.setRecentlyPlayedVideoTime(0.00);
            planClassTodayVo.setRecentlyPlayedVideoProgress(0);
        }
        userPlanVo.setTodayCourse(planClassTodayVo);
        //课程预告
        userPlanVo.setNoticeCourse(makingPlanMapper.queryClassLearnList(userPlanVo.getPlanId(),userPlanVo.getCompleteSchedule()));
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
        //今日课程及继续播放进度
        PlanClassTodayVo planClassTodayVo = makingPlanMapper.queryTodayClassOne(userId);
        planClassTodayVo.setRecentlyPlayedVideoId(0);
        planClassTodayVo.setRecentlyPlayedVideoTime(0.00);
        planClassTodayVo.setRecentlyPlayedVideoProgress(0);
        userPlanVo.setTodayCourse(planClassTodayVo);
        //课程预告
        userPlanVo.setNoticeCourse(makingPlanMapper.queryClassLearnList(userPlan.getPlanId(),1));

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

    @Override
    public ResultUtil addLearningRecord(PlanClassRecord planClassRecord) {
        PlanClassRecord classRecord = makingPlanMapper.queryLearningRecord(planClassRecord.getUserId(),planClassRecord.getPlanClassId(),planClassRecord.getVideoId());
        int i = 0;
        if (classRecord == null){
            //添加课程学习记录
            planClassRecord.setCreateAt(System.currentTimeMillis() / 1000 + "");
            planClassRecord.setUpdateTime(System.currentTimeMillis() / 1000 + "");
            i = makingPlanMapper.addLearningRecord(planClassRecord);
            if (i <= 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
        }
        else {
            //修改课程学习记录
            if(planClassRecord.getWatchTime() > classRecord.getWatchTime()){
                planClassRecord.setId(classRecord.getId());
                planClassRecord.setDifferenceFromLastTime(planClassRecord.getWatchTime()-classRecord.getWatchTime());
                planClassRecord.setUpdateTime(System.currentTimeMillis() / 1000 + "");
                i = makingPlanMapper.updateLearningRecord(planClassRecord);
                if (i <= 0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR);
                }
            }
        }
        return ResultUtil.success(i,"成功",200);
    }

    @Override
    public Object queryMyStudies(int userId) {
        //今日学习时长
        Integer todayTime = makingPlanMapper.queryTodayLearningTime(userId);
        if (todayTime == null){
            todayTime = 0;
        }
        //总学习时长
        int allTime = makingPlanMapper.queryAllLearningTime(userId);
        //今日课程及继续播放进度
        PlanClassTodayVo planClassTodayVo = null;
        UserPlanVo userPlanVo = makingPlanMapper.queryUserPlan(userId); //查找用户计划
        if (userPlanVo.getCompleteSchedule() != 1){
            planClassTodayVo = makingPlanMapper.queryTodayClass(userId);
        }
        else {
            planClassTodayVo = makingPlanMapper.queryTodayClassOne(userId);
        }
        PlanClassRecord classRecord = makingPlanMapper.queryTodayClassRecord(userId, planClassTodayVo.getId());
        if (classRecord != null){
            planClassTodayVo.setRecentlyPlayedVideoId(classRecord.getVideoId());
            planClassTodayVo.setRecentlyPlayedVideoTime(classRecord.getWatchTime());
            planClassTodayVo.setRecentlyPlayedVideoProgress(classRecord.getVideoViewingProgress());
        }
        else {
            planClassTodayVo.setRecentlyPlayedVideoId(0);
            planClassTodayVo.setRecentlyPlayedVideoTime(0.00);
            planClassTodayVo.setRecentlyPlayedVideoProgress(0);
        }
        //最近学习课程（3条）
        List<RecentlyPlanClassVo> recentlyPlanClassVos = makingPlanMapper.queryRecentlyLearnedClass(userId);
        for (RecentlyPlanClassVo vo : recentlyPlanClassVos) {
            if (vo.getVideoViewingProgress() != 0){
                vo.setVideoViewingProgress(vo.getVideoViewingProgress() / (100 * makingPlanMapper.queryClassVideoNum(vo.getId())));
            }
        }
        Map map = new HashMap();
        map.put("todayTime",todayTime);
        map.put("allTime",allTime);
        map.put("planClassToday",planClassTodayVo);
        map.put("recentlyPlanClassList",recentlyPlanClassVos);
        return map;
    }

    @Override
    public List<PlanClass> queryRecentlyLearnedClassList(int userId, Paging paging) {
        Integer page = (paging.getPage() - 1) * paging.getLimit();
        String sql = "limit " + page + "," + paging.getLimit();
        return makingPlanMapper.queryRecentlyLearnedClassList(userId,sql);
    }

    @Override
    public ResultUtil planClassSingIn(int userId, int planId) {
        //判断今天是否已签到
        UserPlan userPlan = makingPlanMapper.queryUserPlanSingRecord(userId, planId);
        if (TimeUtil.getThisTime(Long.parseLong(userPlan.getUpdateTime())) && !userPlan.getUpdateTime().equals(userPlan.getCreateAt())){
            return ResultUtil.errorMsg(250,"今天已完成签到，请明天再来");
        }
        //查询今日课程是否完成
        PlanClassTodayVo planClassTodayVo = null;
        if (userPlan.getCompleteSchedule() != 1){
            planClassTodayVo = makingPlanMapper.queryTodayClass(userId);
        }
        else {
            planClassTodayVo = makingPlanMapper.queryTodayClassOne(userId);
        }
        Integer progress = makingPlanMapper.queryClassViewProgress(userId,planClassTodayVo.getId());
        if(progress == null){
            return ResultUtil.error("尚未完成今日课程");
        }
        Integer videoNum = makingPlanMapper.queryClassVideoNum(planClassTodayVo.getId());
        if (videoNum == null){
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND);
        }
        if((double)progress / (videoNum * 100) >= 0.99){
            //添加签到记录并修改课程学习进度
            SingInVo singInVo = new SingInVo();
            Calendar now = Calendar.getInstance();
            singInVo.setYear(now.get(Calendar.YEAR));
            singInVo.setMonth(now.get(Calendar.MONTH)+1);
            singInVo.setDay(now.get(Calendar.DAY_OF_MONTH));
            singInVo.setType("holiday");
            singInVo.setMark("已签到");
            singInVo.setBgColor("#cce6ff");
            singInVo.setColor("#2a97ff");
            List<SingInVo> singInVos = new ArrayList<>();
            if (userPlan.getSingInRecord() != null && !userPlan.getSingInRecord().equals("")){
                singInVos = JSONArray.parseArray(userPlan.getSingInRecord(), SingInVo.class);
            }
            singInVos.add(singInVo);
            //查询计划有多少天的课程
            int maxDateWeight = makingPlanMapper.queryMaxDateWeightByPlanId(planId);
            if (maxDateWeight == 0){
                throw new ApplicationException(CodeType.RESOURCES_NOT_FIND);
            }
            //如果签到课程为最后一天的课程，则完成进度改为-1，代表已完成计划所有课程的学习
            int completeSchedule = userPlan.getCompleteSchedule();
            if(completeSchedule == maxDateWeight){
                completeSchedule = -1;
            }
            else{
                completeSchedule += 1;
            }
            //修改课程学习进度和签到记录
            int i = makingPlanMapper.updateUserPlanSingInRecord(JSONArray.toJSON(singInVos).toString(),completeSchedule,System.currentTimeMillis() / 1000 + "",userPlan.getId());
            if (i <= 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            return ResultUtil.success("签到打卡成功!");
        }

        return ResultUtil.error("尚未完成今日课程");
    }

    @Override
    public UserPlanVo userPlanReset(int userId,String planOptions) {

        //根据目标参数找到需要的计划id和计划的开始时间
        Plan plan = makingPlanMapper.queryPlanByOptions(planOptions);
        if(plan == null){
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND);
        }
        //修改用户计划数据
        UserPlan userPlan = new UserPlan();
        userPlan.setUserId(userId);
        userPlan.setPlanId(plan.getId());
        userPlan.setCreateAt(System.currentTimeMillis() / 1000 + "");
        userPlan.setUpdateTime(System.currentTimeMillis() / 1000 + "");
        int i = makingPlanMapper.resetUserPlan(userPlan);
        if (i <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"计划重新预设失败");
        }
        //清除用户原有计划的学习（观看）记录 (待定)

        //返回用户计划详情（签到记录以及课程学习列表）
        UserPlanVo userPlanVo = new UserPlanVo();
        userPlanVo.setPlanId(userPlan.getPlanId());
        userPlanVo.setUserId(userId);
        //今日课程及继续播放进度
        PlanClassTodayVo planClassTodayVo = makingPlanMapper.queryTodayClassOne(userId);
        planClassTodayVo.setRecentlyPlayedVideoId(0);
        planClassTodayVo.setRecentlyPlayedVideoTime(0.00);
        planClassTodayVo.setRecentlyPlayedVideoProgress(0);
        userPlanVo.setTodayCourse(planClassTodayVo);
        //课程预告
        userPlanVo.setNoticeCourse(makingPlanMapper.queryClassLearnList(userPlan.getPlanId(),1));

        return userPlanVo;
    }
}
