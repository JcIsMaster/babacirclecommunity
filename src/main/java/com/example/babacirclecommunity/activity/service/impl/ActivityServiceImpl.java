package com.example.babacirclecommunity.activity.service.impl;

import com.example.babacirclecommunity.activity.dao.ActivityMapper;
import com.example.babacirclecommunity.activity.entity.Activity;
import com.example.babacirclecommunity.activity.entity.ActivityParticipate;
import com.example.babacirclecommunity.activity.service.IActivityService;
import com.example.babacirclecommunity.activity.vo.ActivityListVo;
import com.example.babacirclecommunity.circle.dao.CircleMapper;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.common.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author JC
 * @date 2021/9/15 15:10
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ActivityServiceImpl implements IActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private CircleMapper circleMapper;

    @Override
    public List<ActivityListVo> queryActivityList(String area,Paging paging) {
        return activityMapper.queryActivityList(area,getPaging(paging));
    }

    @Override
    public Activity queryActivityDetailsById(int id, int userId) {
        Activity activity = activityMapper.queryActivityDetailsById(id);
        if(activity == null){
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND);
        }
        //查看用户是否参与该活动
        int whetherToParticipate = activityMapper.queryWhetherToParticipateInActivity(activity.getId(),userId);
        if(whetherToParticipate != 0){
            activity.setParticipationStatus(1);
        }
        return activity;
    }

    @Override
    public int participateInActivity(ActivityParticipate activityParticipate) {
        activityParticipate.setCreateAt(System.currentTimeMillis() / 1000 + "");
        return activityMapper.participateInActivity(activityParticipate);
    }

    @Override
    public ResultUtil createActivity(Activity activity) throws ParseException {
        //查询是否为圈主，否则不允许创建
        int circleCount = circleMapper.myCircleCount(activity.getSponsorUserId());
        if (circleCount == 0){
            ResultUtil.error("您还不是圈主");
        }
        activity.setActivityStartTime(System.currentTimeMillis() / 1000 + "");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        activity.setActivityEndTime(String.valueOf(TimeUtil.getEndOfDay(simpleDateFormat.parse(activity.getActivityEndTime()))));
        int i = activityMapper.createActivity(activity);
        if (i <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"创建活动失败");
        }
        return ResultUtil.success(i);
    }

    @Override
    public List<ActivityListVo> queryMythActivity(int userId,int type,Paging paging) {
        return activityMapper.queryMythActivity(userId,type,getPaging(paging));
    }

    @Override
    public List<ActivityListVo> queryMyParticipatedActivity(int userId, Paging paging) {
        return activityMapper.queryMyParticipatedActivity(userId, getPaging(paging));
    }

    /**
     * 分页获取
     * @param paging
     * @return
     */
    public String getPaging(Paging paging) {
        int page = (paging.getPage() - 1) * paging.getLimit();
        return "limit " + page + "," + paging.getLimit();
    }

    /**
     * 查询到期活动进行截至操作
     * 秒   分   时     日   月   周几
     * 0    *    *     *   *     0-7
     */
    @Scheduled(cron = "5 0 0 * * ?")
    public void activityDeadline(){
        //查询进行中的活动
        List<Activity> activities = activityMapper.queryNotDueActivity();
        //获取当前时间戳
        long currentTime = System.currentTimeMillis() / 1000;
        for (Activity activity : activities) {
            //如果活动报名结束时间已过，停止活动
            if (currentTime > Long.parseLong(activity.getActivityEndTime())){
                int i = activityMapper.dueActivityById(activity.getId());
                if (i <= 0){
                    log.error("活动id:" + activity.getId() + ",到期活动截止失败,时间:" + currentTime);
                }
            }
        }
        log.info("到期活动进行截至操作,时间:" + currentTime);
    }

}
