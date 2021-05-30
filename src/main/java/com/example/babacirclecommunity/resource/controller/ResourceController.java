package com.example.babacirclecommunity.resource.controller;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.resource.service.IResourceService;
import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
import com.example.babacirclecommunity.resource.vo.ResourcesVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * @author MQ
 * @date 2021/5/27 17:55
 */
@Api(tags = "资源API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/ResourceController")
public class ResourceController {

    @Autowired
    private IResourceService iResourceService;

    /**
     *
     * 查询资源数据
     * @return
     */
    @ApiOperation(value = "查询资源数据",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryResource")
    public List<ResourceClassificationVo> queryResource(Paging paging,int orderRule,int tagId, String title){
        return iResourceService.queryResource(paging,orderRule,tagId,title);
    }

    /**
     * 查询单个资源帖子
     * @param id 资源帖子id
     * @return
     */
    @ApiOperation(value = "查询单个资源帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectSingleResourcePost")
    public ResourcesVo selectSingleResourcePost(int id, int userId) throws ParseException {
        return iResourceService.selectSingleResourcePost(id,userId);
    }

    @ApiOperation(value = "根据id查询他人发布的货源帖子", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryHavePostedPosts")
    public List<ResourceClassificationVo> queryHavePostedPosts(int othersId,Paging paging) {
        return iResourceService.queryHavePostedPosts(othersId,paging);
    }

    /**
     * 根据二级标签id查询推荐数据 点进帖子详情 触发
     * @param id 二级标签id
     * @return
     */
    @ApiOperation(value = "根据二级标签id查询推荐的数据",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectRecommendedSecondaryTagId")
    public List<ResourceClassificationVo> selectRecommendedSecondaryTagId(int id,int userId,int tid)  {
        return iResourceService.selectRecommendedSecondaryTagId(id,userId,tid);
    }



}
