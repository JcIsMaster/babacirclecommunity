package com.example.babacirclecommunity.gold.controller;


import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.gold.entity.GoldCoinChange;
import com.example.babacirclecommunity.gold.entity.PostExceptional;
import com.example.babacirclecommunity.gold.service.IGoldService;
import com.example.babacirclecommunity.gold.vo.SingInVo;
import com.example.babacirclecommunity.gold.vo.UserGoldCoinsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * @author MQ
 * @date 2021/4/13 14:24
 */
@Api(tags = "金币API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/GoldController")
public class GoldController {

    @Autowired
    private IGoldService iGoldService;


    /**
     * 打赏帖子
     * @return
     */
    @ApiOperation(value = "打赏帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/postExceptional")
    public ResultUtil postExceptional(int rewardedUserId, PostExceptional postExceptional){
        if(rewardedUserId==0 || postExceptional.getUId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }

        return iGoldService.postExceptional(rewardedUserId, postExceptional);
    }

    /**
     * 签到
     * @return
     */
    @ApiOperation(value = "签到",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/signIn")
    public void signIn(int userId,int goldNumber){
        if(userId==0 || goldNumber==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
         iGoldService.signIn(userId,goldNumber);
    }

    /**
     * 查询签到的数据
     * @return
     */
    @ApiOperation(value = "查询签到的数据",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryCheckedInData")
    public UserGoldCoinsVo queryCheckedInData(Integer userId) throws ParseException {
        return iGoldService.queryCheckedInData(userId);
    }


    /**
     * 查询金币变化数据
     * @return
     */
    @ApiOperation(value = "查询金币变化数据",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryGoldCoinChange")
    public List<GoldCoinChange> queryGoldCoinChange(Integer userId, Paging paging) {
        if(paging.getPage()==0 || userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"page不要传0传1");
        }
        return iGoldService.queryGoldCoinChange(userId,paging);
    }

    /**
     * 查询金币变化数据
     * @return
     */
    @ApiOperation(value = "查询签到",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/querySign")
    public List<SingInVo> querySign(Integer userId) {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iGoldService.querySign(userId);
    }

}
