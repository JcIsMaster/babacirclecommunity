package com.example.babacirclecommunity.activity.service;

import com.example.babacirclecommunity.activity.entity.ActivityOnline;
import com.example.babacirclecommunity.activity.entity.ActivityOnlineDiscount;
import com.example.babacirclecommunity.activity.entity.ActivityOnlineOrder;
import com.example.babacirclecommunity.activity.entity.ActivityOnlineParticipate;
import com.example.babacirclecommunity.activity.vo.ActivityOnlineDiscountVo;
import com.example.babacirclecommunity.activity.vo.ActivityOnlineHelpInfoVo;
import com.example.babacirclecommunity.activity.vo.ActivityOnlineListVo;
import com.example.babacirclecommunity.activity.vo.ActivityOnlineVo;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;

import java.text.ParseException;
import java.util.List;

/**
 * @author JC
 * @date 2021/10/6 11:39
 */
public interface IActivityOnlineService {


    /**
     * 查询线上活动列表
     * @param activityLevel
     * @param paging
     * @return
     */
    List<ActivityOnlineListVo> queryActivityOnlineList(int activityLevel,Paging paging);

    /**
     * 查询线上活动详情
     * @param id
     * @return
     */
    ActivityOnlineVo queryActivityOnlineDetailsById(int id);

    /**
     * 创建线上活动验证
     * @param userId
     * @param honoredLevel
     * @return
     */
    ResultUtil createActivityOnlineVerify(int userId,int honoredLevel);

    /**
     * 创建线上活动
     * @param activityOnline
     * @param honoredLevel
     * @return
     * @throws ParseException
     */
    ResultUtil createActivityOnline(ActivityOnline activityOnline,int honoredLevel) throws ParseException;

    /**
     * 用户参加线上活动
     * @param activityOnlineParticipate
     * @return
     * @throws ParseException
     */
    ResultUtil participateActivityOnline(ActivityOnlineParticipate activityOnlineParticipate);

    /**
     * 查询帮砍价列表
     * @param onlineParticipateId
     * @param paging
     * @return
     */
    List<ActivityOnlineDiscountVo> queryActivityOnlineDiscountVo(int onlineParticipateId, Paging paging);

    /**
     * 帮砍价(助力)
     * @param activityOnlineDiscount
     * @return
     */
    ResultUtil helpBargain(ActivityOnlineDiscount activityOnlineDiscount);

    /**
     * 活动(助力)详情页
     * @param onlineParticipateId
     * @return
     */
    ResultUtil helpBargainInfo(int onlineParticipateId);

    /**
     * 购买活动品
     * @param activityOnlineOrder
     * @return
     */
    ResultUtil buyActivityItems(ActivityOnlineOrder activityOnlineOrder);

    /**
     * 查询活动订单
     * @param userId
     * @param paging
     * @return
     */
    ResultUtil queryActivityOrders(int userId,Paging paging);
}
