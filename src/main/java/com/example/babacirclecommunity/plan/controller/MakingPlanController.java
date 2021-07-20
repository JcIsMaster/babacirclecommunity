package com.example.babacirclecommunity.plan.controller;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.plan.entity.Plan;
import com.example.babacirclecommunity.plan.entity.PlanClass;
import com.example.babacirclecommunity.plan.entity.PlanClassFeedback;
import com.example.babacirclecommunity.plan.entity.UserPlan;
import com.example.babacirclecommunity.plan.service.IMakingPlanService;
import com.example.babacirclecommunity.plan.vo.PlanClassVo;
import com.example.babacirclecommunity.plan.vo.TopicOptionsVo;
import com.example.babacirclecommunity.plan.vo.UserPlanVo;
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
}
