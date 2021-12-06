package com.example.babacirclecommunity.honored.service.impl;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.honored.dao.HonoredMapper;
import com.example.babacirclecommunity.honored.entity.Honored;
import com.example.babacirclecommunity.honored.service.IHonoredService;
import com.example.babacirclecommunity.honored.vo.UserHonoredCenterVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JC
 * @date 2021/11/10 11:47
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class HonoredServiceImpl implements IHonoredService {

    @Autowired
    private HonoredMapper honoredMapper;

    @Override
    public UserHonoredCenterVo queryUserHonoredCenter(int userId) {
        UserHonoredCenterVo honoredCenterVo = new UserHonoredCenterVo();
        //查询用户荣誉等级信息
        Honored honored = honoredMapper.queryUserHonored(userId);
        if (honored == null) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND);
        }
        honoredCenterVo.setHonored(honored);
        //查询用户今日荣誉积分任务完成状态
        //匹配
        int userMatch = honoredMapper.queryParameterById(userId);
        if (userMatch != 0){
            honoredCenterVo.setMatchStatus(1);
        }
        //发帖
        int todayPostCount = honoredMapper.queryTodayCircleCount(userId);
        if (todayPostCount != 0){
            honoredCenterVo.setPostStatus(1);
        }
        //人才
        int talent = honoredMapper.queryTalentById(userId);
        if (talent != 0){
            honoredCenterVo.setTalentsStatus(1);
        }
        //货源
        int todayResourceCount = honoredMapper.queryTodayPostedPostsCount(userId,12);
        if (todayResourceCount != 0){
            honoredCenterVo.setResourceStatus(1);
        }
        //合作
        int todayCollaborateCount =honoredMapper.queryTodayPostedPostsCount(userId,13);
        if (todayCollaborateCount != 0){
            honoredCenterVo.setCollaborateStatus(1);
        }
        return honoredCenterVo;
    }
}
