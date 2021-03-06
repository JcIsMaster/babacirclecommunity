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
        //?????????????????????????????????
        PlanClassTodayVo planClassTodayVo = null;
        if (userPlanVo.getCompleteSchedule() == -1){
            //????????????????????????????????????
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
        //????????????
        userPlanVo.setNoticeCourse(makingPlanMapper.queryClassLearnList(userPlanVo.getPlanId(),userPlanVo.getCompleteSchedule()));
        return userPlanVo;
    }

    @Override
    public UserPlanVo generatePlan(int userId,String planOptions) {
        //???????????????????????????????????????id????????????????????????
        Plan plan = makingPlanMapper.queryPlanByOptions(planOptions);
        if(plan == null){
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND);
        }
        //????????????????????????
        UserPlan userPlan = new UserPlan();
        userPlan.setUserId(userId);
        userPlan.setPlanId(plan.getId());
        userPlan.setCreateAt(System.currentTimeMillis() / 1000 + "");
        int i = makingPlanMapper.generatePlan(userPlan);
        if (i <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        //??????????????????????????????????????????????????????????????????
        UserPlanVo userPlanVo = new UserPlanVo();
        userPlanVo.setPlanId(userPlan.getPlanId());
        userPlanVo.setUserId(userId);
        //?????????????????????????????????
        PlanClassTodayVo planClassTodayVo = makingPlanMapper.queryTodayClassOne(userId);
        planClassTodayVo.setRecentlyPlayedVideoId(0);
        planClassTodayVo.setRecentlyPlayedVideoTime(0.00);
        planClassTodayVo.setRecentlyPlayedVideoProgress(0);
        userPlanVo.setTodayCourse(planClassTodayVo);
        //????????????
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
            //????????????????????????
            planClassRecord.setCreateAt(System.currentTimeMillis() / 1000 + "");
            planClassRecord.setUpdateTime(System.currentTimeMillis() / 1000 + "");
            i = makingPlanMapper.addLearningRecord(planClassRecord);
            if (i <= 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
        }
        else {
            //????????????????????????
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
        return ResultUtil.success(i,"??????",200);
    }

    @Override
    public Object queryMyStudies(int userId) {
        //??????????????????
        Integer todayTime = makingPlanMapper.queryTodayLearningTime(userId);
        if (todayTime == null){
            todayTime = 0;
        }
        //???????????????
        int allTime = makingPlanMapper.queryAllLearningTime(userId);
        //?????????????????????????????????
        PlanClassTodayVo planClassTodayVo = null;
        UserPlanVo userPlanVo = makingPlanMapper.queryUserPlan(userId); //??????????????????
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
        //?????????????????????3??????
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
        //???????????????????????????
        UserPlan userPlan = makingPlanMapper.queryUserPlanSingRecord(userId, planId);
        if (TimeUtil.getThisTime(Long.parseLong(userPlan.getUpdateTime())) && !userPlan.getUpdateTime().equals(userPlan.getCreateAt())){
            return ResultUtil.errorMsg(250,"???????????????????????????????????????");
        }
        //??????????????????????????????
        PlanClassTodayVo planClassTodayVo = null;
        if (userPlan.getCompleteSchedule() != 1){
            planClassTodayVo = makingPlanMapper.queryTodayClass(userId);
        }
        else {
            planClassTodayVo = makingPlanMapper.queryTodayClassOne(userId);
        }
        Integer progress = makingPlanMapper.queryClassViewProgress(userId,planClassTodayVo.getId());
        if(progress == null){
            return ResultUtil.error("????????????????????????");
        }
        Integer videoNum = makingPlanMapper.queryClassVideoNum(planClassTodayVo.getId());
        if (videoNum == null){
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND);
        }
        if((double)progress / (videoNum * 100) >= 0.99){
            //?????????????????????????????????????????????
            SingInVo singInVo = new SingInVo();
            Calendar now = Calendar.getInstance();
            singInVo.setYear(now.get(Calendar.YEAR));
            singInVo.setMonth(now.get(Calendar.MONTH)+1);
            singInVo.setDay(now.get(Calendar.DAY_OF_MONTH));
            singInVo.setType("holiday");
            singInVo.setMark("?????????");
            singInVo.setBgColor("#cce6ff");
            singInVo.setColor("#2a97ff");
            List<SingInVo> singInVos = new ArrayList<>();
            if (userPlan.getSingInRecord() != null && !userPlan.getSingInRecord().equals("")){
                singInVos = JSONArray.parseArray(userPlan.getSingInRecord(), SingInVo.class);
            }
            singInVos.add(singInVo);
            //?????????????????????????????????
            int maxDateWeight = makingPlanMapper.queryMaxDateWeightByPlanId(planId);
            if (maxDateWeight == 0){
                throw new ApplicationException(CodeType.RESOURCES_NOT_FIND);
            }
            //??????????????????????????????????????????????????????????????????-1?????????????????????????????????????????????
            int completeSchedule = userPlan.getCompleteSchedule();
            if(completeSchedule == maxDateWeight){
                completeSchedule = -1;
            }
            else{
                completeSchedule += 1;
            }
            //???????????????????????????????????????
            int i = makingPlanMapper.updateUserPlanSingInRecord(JSONArray.toJSON(singInVos).toString(),completeSchedule,System.currentTimeMillis() / 1000 + "",userPlan.getId());
            if (i <= 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            return ResultUtil.success("??????????????????!");
        }

        return ResultUtil.error("????????????????????????");
    }

    @Override
    public UserPlanVo userPlanReset(int userId,String planOptions) {

        //???????????????????????????????????????id????????????????????????
        Plan plan = makingPlanMapper.queryPlanByOptions(planOptions);
        if(plan == null){
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND);
        }
        //????????????????????????
        UserPlan userPlan = new UserPlan();
        userPlan.setUserId(userId);
        userPlan.setPlanId(plan.getId());
        userPlan.setCreateAt(System.currentTimeMillis() / 1000 + "");
        userPlan.setUpdateTime(System.currentTimeMillis() / 1000 + "");
        int i = makingPlanMapper.resetUserPlan(userPlan);
        if (i <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"????????????????????????");
        }
        //??????????????????????????????????????????????????? (??????)

        //??????????????????????????????????????????????????????????????????
        UserPlanVo userPlanVo = new UserPlanVo();
        userPlanVo.setPlanId(userPlan.getPlanId());
        userPlanVo.setUserId(userId);
        //?????????????????????????????????
        PlanClassTodayVo planClassTodayVo = makingPlanMapper.queryTodayClassOne(userId);
        planClassTodayVo.setRecentlyPlayedVideoId(0);
        planClassTodayVo.setRecentlyPlayedVideoTime(0.00);
        planClassTodayVo.setRecentlyPlayedVideoProgress(0);
        userPlanVo.setTodayCourse(planClassTodayVo);
        //????????????
        userPlanVo.setNoticeCourse(makingPlanMapper.queryClassLearnList(userPlan.getPlanId(),1));

        return userPlanVo;
    }
}
