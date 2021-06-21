package com.example.babacirclecommunity.circle.controller;

import com.example.babacirclecommunity.circle.entity.Circle;
import com.example.babacirclecommunity.circle.entity.CommunityUser;
import com.example.babacirclecommunity.circle.service.ICircleService;
import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.circle.vo.CircleVo;
import com.example.babacirclecommunity.circle.vo.CommunityVo;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.home.entity.Community;
import com.example.babacirclecommunity.user.vo.UserVo;
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
 * @date 2021/5/20 19:38
 */
@Api(tags = "圈子API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/CircleController")
public class CircleController {

   @Autowired
   private ICircleService iCircleService;

    /**
     *
     * 查询我关注的人发的帖子
     * @return
     */
    @ApiOperation(value = "查询我关注的人发的帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryPostsPeopleFollow")
    public List<CircleClassificationVo> queryPostsPeopleFollow(int userId, Paging paging){
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iCircleService.queryPostsPeopleFollow(userId,paging);
    }


    /**
     * 查询视频或者图片帖子
     * @return
     */
    @ApiOperation(value = "查询视频或者图片帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryImagesOrVideos")
    public List<CircleClassificationVo> queryImagesOrVideos(int type,Paging paging,int userId)  {
        return iCircleService.queryImagesOrVideos(type,paging,userId);
    }

    /**
     *
     * 查询单个圈子帖子
     * @return
     */
    @ApiOperation(value = "查询单个圈子的帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/querySingleCircle")
    public CircleClassificationVo querySingleCircle(int id,int userId) throws ParseException {
        if(id==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return  iCircleService.querySingleCircle(id, userId);
    }

    /**
     *
     * 查询推荐圈子
     * @return
     */
    @ApiOperation(value = "查询推荐圈子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryReferenceCircles")
    public List<CircleClassificationVo> queryReferenceCircles(int userId, Paging paging)  {
        return  iCircleService.queryReferenceCircles(userId,paging);
    }

    /**
     *
     * 查询我的圈子 查询我创建的圈子 （圈子广场）
     * @return
     */
    @ApiOperation(value = "查询我的圈子 （圈子广场）",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryCheckMyCirclesSquare")
    public List<CircleVo> queryCheckMyCirclesSquare(int userId,String communityName, Paging paging)  {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return  iCircleService.queryCheckMyCirclesSquare(userId,communityName,paging);
    }

    /**
     *
     * 发现圈子
     * @return
     */
    @ApiOperation(value = "发现圈子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/fundCircle")
    public Map<String,Object> fundCircle(int userId, String communityName,Paging paging)  {
        return  iCircleService.fundCircle(userId,communityName,paging);
    }



    /**
     * 发布圈子
     * @return
     */
    @ApiOperation(value = "发布圈子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/publishingCircles")
    public void publishingCircles(Circle circle, String imgUrl) throws Exception {
        if(circle.getUserId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        iCircleService.publishingCircles(circle, imgUrl);
    }


    /**
     * 创建圈子
     * @return
     */
    @ApiOperation(value = "创建圈子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addCircle")
    public void addCircle(Community community) throws ParseException {
        iCircleService.addCircle(community);
    }


    /**
     * 进入单元体的接口
     * 根据社区分类id查询圈子信息
     * @return
     */
    @ApiOperation(value = "根据社区分类id查询圈子信息 ",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectCommunityCategoryId")
    public CommunityVo selectCommunityCategoryId(int id, int userId)  {
        return iCircleService.selectCommunityCategoryId(id,userId);
    }


    @ApiOperation(value = "单元体导航栏点击查询", notes = "成功返回集合")
    @ResponseBody
    @PostMapping("/queryClickUnitNavigationBar")
    public List<CircleClassificationVo> queryClickUnitNavigationBar(int typeId,int userId,int tagId,Paging paging) {
        return iCircleService.queryClickUnitNavigationBar(typeId,userId,tagId,paging);
    }

    /**
     * 加入圈子
     * @return
     */
    @ApiOperation(value = "加入圈子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/joinCircle")
    public int joinCircle(CommunityUser communityUser)  {
        if(communityUser.getUserId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"请先登录！");
        }
        return iCircleService.joinCircle(communityUser);
    }

    /**
     * 修改圈子信息
     * @return
     */
    @ApiOperation(value = "修改圈子信息",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/updateCircle")
    public void updateCircle(Community community)  {
        if(community.getPosters()==null || "undefined".equals(community.getPosters())){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"请上传封面");
        }
         iCircleService.updateCircle(community);
    }

    /**
     * 成员管理
     * @return
     */
    @ApiOperation(value = "成员管理",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/memberManagement")
    public void memberManagement(int communityId,int otherId)  {
        iCircleService.memberManagement(communityId,otherId);
    }

    /**
     * 查询圈子成员
     * @return
     */
    @ApiOperation(value = "查询圈子成员",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryCircleMembers")
    public List<UserVo> queryCircleMembers(int communityId,int userId)  {
        return iCircleService.queryCircleMembers(communityId,userId);
    }

    /**
     * 添加单元体标签
     * @return
     */
    @ApiOperation(value = "添加单元体标签",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addTagHaplont")
    public void addTagHaplont(int tagId,String hName){
         iCircleService.addTagHaplont(tagId,hName);
    }



}
