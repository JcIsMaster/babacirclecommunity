package com.example.babacirclecommunity.plan.controller;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.data.Result;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.plan.entity.*;
import com.example.babacirclecommunity.plan.service.IMakingPlanService;
import com.example.babacirclecommunity.plan.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author JC
 * @date 2021/7/12 15:03
 */
@Api(tags = "生成计划API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/MakingPlanController")
public class MakingPlanController {

    @Autowired
    private IMakingPlanService iMakingPlanService;

    @ApiOperation(value = "查询目标参数题目和选项信息",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/getTopicAndOptions")
    public List<TopicOptionsVo> getTopicAndOptions(){
        return iMakingPlanService.getTopicAndOptions();
    }

    @ApiOperation(value = "查看用户计划",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryUserPlan")
    public UserPlanVo queryUserPlan(int userId) throws Exception{
        if (userId == 0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iMakingPlanService.queryUserPlan(userId);
    }

    @ApiOperation(value = "生成(新增)用户计划",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/generatePlan")
    public UserPlanVo generatePlan(int userId,String planOptions) throws Exception{
        if (userId == 0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iMakingPlanService.generatePlan(userId,planOptions);
    }

    @ApiOperation(value = "查询计划课程详情",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryPlanClassDetail")
    public PlanClassVo queryPlanClassDetail(int planClassId){
        return iMakingPlanService.queryPlanClassDetail(planClassId);
    }

    @ApiOperation(value = "新增计划课程反馈",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addPlanFeedBack")
    public void addPlanFeedBack(PlanClassFeedback planClassFeedback){
        iMakingPlanService.addPlanFeedBack(planClassFeedback);
    }

    @ApiOperation(value = "根据tagId查询增强计划列表",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryEnhancePlanListByTag")
    public List<PlanClass> queryEnhancePlanListByTag(int tagId, Paging paging){
        return iMakingPlanService.queryEnhancePlanListByTag(tagId,paging);
    }

    @ApiOperation(value = "添加课程学习记录",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addLearningRecord")
    public ResultUtil addLearningRecord(PlanClassRecord planClassRecord){
        return iMakingPlanService.addLearningRecord(planClassRecord);
    }

    @ApiOperation(value = "查询我的学习",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryMyStudies")
    public Object queryMyStudies(int userId){
        if (userId == 0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iMakingPlanService.queryMyStudies(userId);
    }

    @ApiOperation(value = "查询最近学习课程列表",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryRecentlyLearnClass")
    public List<PlanClass> queryRecentlyLearnClass(int userId, Paging paging){
        return iMakingPlanService.queryRecentlyLearnedClassList(userId,paging);
    }

    @ApiOperation(value = "课程打卡签到",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/planClassSingIn")
    public ResultUtil planClassSingIn(int userId,int planId){
        return iMakingPlanService.planClassSingIn(userId,planId);
    }

    @ApiOperation(value = "用户重新预设计划",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/userPlanReset")
    public UserPlanVo userPlanReset(int userId,String planOptions){
        return iMakingPlanService.userPlanReset(userId,planOptions);
    }
}
