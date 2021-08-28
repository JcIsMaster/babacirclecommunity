package com.example.babacirclecommunity.resource.controller;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.resource.entity.Collection;
import com.example.babacirclecommunity.resource.entity.Resources;
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
import java.util.Map;

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
    public Map<String,Object> queryHavePostedPosts(int userId,int othersId, Paging paging) {
        return iResourceService.queryHavePostedPosts(userId,othersId,paging);
    }

    @ApiOperation(value = "编辑用户资源介绍", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/editUserResourceIntroduce")
    public ResultUtil editUserResourceIntroduce(int userId,String introduce){
        return iResourceService.editUserResourceIntroduce(userId, introduce);
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

    /**
     * 根据一级标签id查询所有视频
     * @param id 一级标签id
     * @return
     */
    @ApiOperation(value = "根据一级标签id查询所有视频",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryAllVideosPrimaryTagId")
    public List<ResourcesVo> queryAllVideosPrimaryTagId(int id, Paging paging,int userId) throws ParseException {
        return iResourceService.queryAllVideosPrimaryTagId(id,paging,userId);
    }

    @ApiOperation(value = "收藏帖子", notes = "成功返回集合")
    @ResponseBody
    @PostMapping("/collectionPost")
    public int collectionPost(Collection collection) {
        if(collection.getUserId()==0 || collection.getTId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iResourceService.collectionPost(collection);
    }

    /**
     * 发布
     * @return
     */
    @ApiOperation(value = "发布",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/issueResourceOrCircle")
    public int issueResourceOrCircle(Resources resources, String imgUrl) throws Exception {
        if(resources.getUId()==0 || resources.getTagsTwo()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iResourceService.issueResourceOrCircle(resources,imgUrl);
    }


    /**
     * 得到海报图
     * @return
     */
    @ApiOperation(value = "得到海报图",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/getPosterImage")
    public List<String> getPosterImage(String pageUrl,String id) throws Exception {
        return iResourceService.getPosterImage(pageUrl,id);
    }

    @ApiOperation(value = "删除货源",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/deleteResourceById")
    public void deleteResourceById(int id){
        iResourceService.deleteResourceById(id);
    }
}
