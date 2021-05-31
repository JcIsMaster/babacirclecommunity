package com.example.babacirclecommunity.collaborate.controller;

import com.example.babacirclecommunity.collaborate.service.ICollaborateService;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.resource.entity.Collection;
import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
import com.example.babacirclecommunity.resource.vo.ResourcesVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author MQ
 * @date 2021/5/31 10:32
 */
@Api(tags = "合作API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/CollaborateController")
public class CollaborateController {

    @Autowired
    private ICollaborateService iCollaborateService;

    /**
     *
     * 查询合作数据
     * @return
     */
    @ApiOperation(value = "查询合作数据",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryCollaborate")
    public List<ResourceClassificationVo> queryCollaborate(Paging paging, int orderRule, int tagId, String title){
        return iCollaborateService.queryCollaborate(paging,orderRule,tagId,title);
    }

    /**
     * 查询单个合作帖子
     * @param id 资合作帖子id
     * @return
     */
    @ApiOperation(value = "查询单个合作帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/querySingleCollaboratePost")
    public ResourcesVo querySingleCollaboratePost(int id, int userId) throws ParseException {
        return iCollaborateService.querySingleCollaboratePost(id,userId);
    }

    @ApiOperation(value = "根据id查询他人发布的合作帖子", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryHaveCollaboratePostedPosts")
    public Map<String,Object> queryHaveCollaboratePostedPosts(int userId, int othersId, Paging paging) {
        return iCollaborateService.queryHaveCollaboratePostedPosts(userId,othersId,paging);
    }

    /**
     * 根据二级标签id查询推荐数据 点进帖子详情 触发
     * @param id 二级标签id
     * @return
     */
    @ApiOperation(value = "根据二级标签id查询推荐的数据",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryRecommendedSecondaryTagId")
    public List<ResourceClassificationVo> queryRecommendedSecondaryTagId(int id,int userId,int tid)  {
        return iCollaborateService.queryRecommendedSecondaryTagId(id,userId,tid);
    }

    /**
     * 根据一级标签id查询所有视频
     * @param id 一级标签id
     * @return
     */
    @ApiOperation(value = "根据一级标签id查询所有视频",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryAllCollaborateVideosPrimaryTagId")
    public List<ResourcesVo> queryAllCollaborateVideosPrimaryTagId(int id, Paging paging,int userId) throws ParseException {
        return iCollaborateService.queryAllCollaborateVideosPrimaryTagId(id,paging,userId);
    }

    @ApiOperation(value = "收藏帖子", notes = "成功返回集合")
    @ResponseBody
    @PostMapping("/collectionCollaboratePost")
    public int collectionCollaboratePost(Collection collection) {
        if(collection.getUserId()==0 || collection.getTId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iCollaborateService.collectionCollaboratePost(collection);
    }

}
