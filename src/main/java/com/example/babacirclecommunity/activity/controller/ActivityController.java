package com.example.babacirclecommunity.activity.controller;

import com.example.babacirclecommunity.activity.entity.Activity;
import com.example.babacirclecommunity.activity.entity.ActivityParticipate;
import com.example.babacirclecommunity.activity.service.IActivityService;
import com.example.babacirclecommunity.activity.vo.ActivityListVo;
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
 * @date 2021/9/15 15:02
 */
@Api(tags = "活动API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/ActivityController")
public class ActivityController {

    @Autowired
    private IActivityService iActivityService;

    @ApiOperation(value = "查询活动列表",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryActivityList")
    public List<ActivityListVo> queryActivityList(String area,Paging paging){
        return iActivityService.queryActivityList(area,paging);
    }

    @ApiOperation(value = "查询活动详情",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryActivityDetailsById")
    public Activity queryActivityDetailsById(int id,int userId){
        return iActivityService.queryActivityDetailsById(id,userId);
    }

    @ApiOperation(value = "用户参与活动",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/participateInActivity")
    public int participateInActivity(ActivityParticipate activityParticipate){
        return iActivityService.participateInActivity(activityParticipate);
    }

    @ApiOperation(value = "创建活动",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/createActivity")
    public ResultUtil createActivity(Activity activity) throws ParseException {
        return iActivityService.createActivity(activity);
    }
}
