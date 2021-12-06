package com.example.babacirclecommunity.common.utils;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.constanct.PointsType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.honored.dao.HonoredMapper;
import com.example.babacirclecommunity.honored.entity.HonoredPoints;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author JC
 * @date 2021/11/10 11:51
 */
@Slf4j
@Component
public class HonoredPointsUtil {

    @Resource
    private HonoredMapper honoredMapper;

    public static HonoredPointsUtil honoredPointsUtil;

    @PostConstruct
    public void init() {
        honoredPointsUtil = this;
        honoredPointsUtil.honoredMapper = this.honoredMapper;
    }

    public static void addHonoredPoints(int userId,PointsType pointsType,int type,String createTime){
        if (pointsType.getType() == 1) {
            //判断是否是当天首次发帖
            int circleCount = honoredPointsUtil.honoredMapper.queryTodayCircleCount(userId);
            if (circleCount != 1) {
                return;
            }
        }
        if (pointsType.getType() == 3) {
            //判断是否是当天首次发货源
            int resourceCount = honoredPointsUtil.honoredMapper.queryTodayPostedPostsCount(userId,12);
            if (resourceCount != 1) {
                return;
            }
        }
        if (pointsType.getType() == 4) {
            //判断是否是当天首次发合作
            int collaborateCount = honoredPointsUtil.honoredMapper.queryTodayPostedPostsCount(userId,13);
            if (collaborateCount != 1) {
                return;
            }
        }
        int i = honoredPointsUtil.honoredMapper.addHonoredPoints(new HonoredPoints(userId,pointsType.getPoints(),type,pointsType.getType(),createTime));
        if (i <= 0) {
            log.info("积分获取异常,来源: " + pointsType.getType() + "。异常用户: " + userId);
            throw new ApplicationException(CodeType.SERVICE_ERROR,"积分获取异常");
        }
    }

    public static void addHonoredPointsForRecharge(int userId,int points,int source,int type,String createTime){
        int i = honoredPointsUtil.honoredMapper.addHonoredPoints(new HonoredPoints(userId,points,type,source,createTime));
        if (i <= 0) {
            log.info("积分获取异常,来源: " + source + "。异常用户: " + userId);
            throw new ApplicationException(CodeType.SERVICE_ERROR,"积分获取异常");
        }
    }

//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.honoredMapper = applicationContext.getBean(HonoredMapper.class);
//    }
}
