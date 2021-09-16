package com.example.babacirclecommunity.activity.service.impl;

import com.example.babacirclecommunity.activity.dao.ActivityMapper;
import com.example.babacirclecommunity.activity.entity.Activity;
import com.example.babacirclecommunity.activity.entity.ActivityParticipate;
import com.example.babacirclecommunity.activity.service.IActivityService;
import com.example.babacirclecommunity.activity.vo.ActivityListVo;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.TimeUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<ActivityListVo> queryActivityList(Paging paging) {
        return activityMapper.queryActivityList(getPaging(paging));
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
    public int createActivity(Activity activity) throws ParseException {
        activity.setActivityStartTime(System.currentTimeMillis() / 1000 + "");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        activity.setActivityEndTime(String.valueOf(TimeUtil.getEndOfDay(simpleDateFormat.parse(activity.getActivityEndTime()))));
        return activityMapper.createActivity(activity);
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

}
