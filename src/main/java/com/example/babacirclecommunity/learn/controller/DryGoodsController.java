package com.example.babacirclecommunity.learn.controller;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.learn.entity.DryGoods;
import com.example.babacirclecommunity.learn.entity.LearnPostExceptional;
import com.example.babacirclecommunity.learn.service.IDryGoodsService;
import com.example.babacirclecommunity.learn.vo.DryGoodsTagVo;
import com.example.babacirclecommunity.personalCenter.vo.DryGoodsPersonalVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;


/**
 * @author JC
 * @date 2021/4/16 15:49
 */
@Api(tags = "干货API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/DryGoodsController")
public class DryGoodsController {

    @Autowired
    private IDryGoodsService iDryGoodsService;

    /**
     * 根据状态查询学习信息
     * @return
     */
    @ApiOperation(value = "根据状态查询学习信息",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryLearnList")
    public Object queryLearnList(int type, Paging paging, int orderRule,Integer tagId,String content){
        if(paging.getPage()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"page不要传0 或者参数错误");
        }
        return iDryGoodsService.queryLearnList(type,paging,orderRule,tagId,content);
    }

    /**
     * 根据id查询干货详情
     * @param id
     * @param userId
     * @return
     * @throws ParseException
     */
    @ApiOperation(value = "根据id查询干货详情",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryDryGoodsById")
    public DryGoodsTagVo queryDryGoodsById(int id,int userId) throws ParseException {
        if(id==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iDryGoodsService.queryDryGoodsById(id,userId);
    }

    /**
     * 发布干货帖
     * @param dryGoods
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "发布干货帖",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addDryGoods")
    public int addDryGoods(DryGoods dryGoods) throws Exception{
        return iDryGoodsService.addDryGoods(dryGoods);
    }

    /**
     * 干货帖点赞
     * @param id 干货帖子id
     * @param userId 点赞人id
     * @return
     */
    @ApiOperation(value = "点赞",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/giveLike")
    public int giveLike(int id,int userId){
        if(id==0 || userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iDryGoodsService.giveLike(id,userId);
    }

    /**
     * 干货帖收藏
     * @param id 干货帖子id
     * @param userId 收藏人id
     * @return
     */
    @ApiOperation(value = "收藏",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/giveCollect")
    public int giveCollect(int id,int userId){
        if(id==0 || userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iDryGoodsService.giveCollect(id,userId);
    }

    /**
     * 干货帖打赏
     * @param learnPostExceptional 打赏详情对象
     * @return
     */
    @ApiOperation(value = "打赏",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/rewardGoldToDryGoods")
    public ResultUtil rewardGoldToDryGoods(LearnPostExceptional learnPostExceptional) throws Exception{
        return iDryGoodsService.rewardGoldToDryGoods(learnPostExceptional);
    }

    /**
     * 查询干货个人中心
     * @param userId
     * @param otherId
     * @return
     */
    @ApiOperation(value = "查询干货个人中心",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryDryGoodsPersonal")
    public DryGoodsPersonalVo queryDryGoodsPersonal(int userId, int otherId, Paging paging){
        return iDryGoodsService.queryDryGoodsPersonal(userId,otherId,paging);
    }
}
