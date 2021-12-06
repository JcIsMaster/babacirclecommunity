package com.example.babacirclecommunity.activity.controller;

import com.example.babacirclecommunity.activity.entity.*;
import com.example.babacirclecommunity.activity.service.IActivityOnlineService;
import com.example.babacirclecommunity.activity.vo.*;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * @author JC
 * @date 2021/10/6 11:39
 */
@Api(tags = "线上活动API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/ActivityOnlineController")
public class ActivityOnlineController {

    @Autowired
    private IActivityOnlineService iActivityOnlineService;

    @ApiOperation(value = "查询线上活动列表",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryActivityOnlineList")
    public List<ActivityOnlineListVo> queryActivityOnlineList(int activityLevel,Paging paging){
        return iActivityOnlineService.queryActivityOnlineList(activityLevel,paging);
    }

    @ApiOperation(value = "查询线上活动详情",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryActivityOnlineDetailsById")
    public ActivityOnlineVo queryActivityOnlineDetailsById(int id){
        return iActivityOnlineService.queryActivityOnlineDetailsById(id);
    }

    @ApiOperation(value = "创建线上活动验证",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/createActivityOnlineVerify")
    public ResultUtil createActivityOnlineVerify(int userId,int honoredLevel) {
        if (userId == 0) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"未登录");
        }
        return iActivityOnlineService.createActivityOnlineVerify(userId,honoredLevel);
    }

    @ApiOperation(value = "创建线上活动",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/createActivityOnline")
    public ResultUtil createActivityOnline(ActivityOnline activityOnline,int honoredLevel) throws ParseException {
        return iActivityOnlineService.createActivityOnline(activityOnline,honoredLevel);
    }

    @ApiOperation(value = "用户参加线上活动(发起我的砍价)",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/participateActivityOnline")
    public ResultUtil participateActivityOnline(ActivityOnlineParticipate activityOnlineParticipate){
        return iActivityOnlineService.participateActivityOnline(activityOnlineParticipate);
    }

    @ApiOperation(value = "查询帮砍价列表",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryActivityOnlineDiscountVo")
    public List<ActivityOnlineDiscountVo> queryActivityOnlineDiscountVo(int onlineParticipateId,Paging paging) {
        return iActivityOnlineService.queryActivityOnlineDiscountVo(onlineParticipateId,paging);
    }

    @ApiOperation(value = "帮砍价(助力)",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/helpBargain")
    public ResultUtil helpBargain(ActivityOnlineDiscount activityOnlineDiscount) {
        return iActivityOnlineService.helpBargain(activityOnlineDiscount);
    }

    @ApiOperation(value = "活动(助力)详情页",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/helpBargainInfo")
    public ResultUtil helpBargainInfo(int onlineParticipateId) {
        return iActivityOnlineService.helpBargainInfo(onlineParticipateId);
    }

    @ApiOperation(value = "购买活动品",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/buyActivityItems")
    public ResultUtil buyActivityItems(ActivityOnlineOrder activityOnlineOrder){
        return iActivityOnlineService.buyActivityItems(activityOnlineOrder);
    }

    @ApiOperation(value = "查询活动订单",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryActivityOrders")
    public ResultUtil queryActivityOrders(int userId,Paging paging){
        return iActivityOnlineService.queryActivityOrders(userId,paging);
    }

}
