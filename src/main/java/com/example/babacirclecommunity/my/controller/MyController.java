package com.example.babacirclecommunity.my.controller;

import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.my.entity.ComplaintsSuggestions;
import com.example.babacirclecommunity.my.service.IMyService;
import com.example.babacirclecommunity.my.vo.CommentsDifferentVo;
import com.example.babacirclecommunity.user.entity.User;
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
 * @date 2021/6/8 11:14
 */
@Api(tags = "我的API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/MyController")
public class MyController {

    @Autowired
    private IMyService iMyService;

    /**
     * 查询我关注的人
     * @return
     */
    @ApiOperation(value = "查询我关注的人",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryPeopleCareAbout")
    public Map<String,Object> queryPeopleCareAbout(Paging paging, int userId){
        return  iMyService.queryPeopleCareAbout(paging,userId);
    }

    /**
     * 查询我的粉丝
     * @return
     */
    @ApiOperation(value = "查询我的粉丝",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryFan")
    public Map<String,Object> queryFan(Paging paging, int userId){
        return  iMyService.queryFan(paging,userId);
    }


    /**
     * 建议
     * @return
     */
    @ApiOperation(value = "建议",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addComplaintsSuggestions")
    public int addComplaintsSuggestions(ComplaintsSuggestions complaintsSuggestions)  {
        if(complaintsSuggestions.getUserId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iMyService.addComplaintsSuggestions(complaintsSuggestions);
    }

    @ApiOperation(value = "点击头像进入的接口", notes = "成功返回成功")
    @ResponseBody
    @PostMapping("/ClickInterfaceHeadImageEnter")
    public void ClickInterfaceHeadImageEnter(int bUserId, int userId) {
        if(bUserId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
         iMyService.ClickInterfaceHeadImageEnter(bUserId,userId);
    }

    @ApiOperation(value ="查询看过我的人", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryPeopleWhoHaveSeenMe")
    public Map<String,Object> queryPeopleWhoHaveSeenMe(int userId,Paging paging) {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iMyService.queryPeopleWhoHaveSeenMe(userId,paging);
    }

    @ApiOperation(value = "修改用户信息", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/updateUserInformation")
    public int updateUserInformation(User user) throws ParseException {
        if(user.getId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        int i=iMyService.updateUserInformation(user);
        return i;
    }

    @ApiOperation(value = "根据状态查询不同模块的评论", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryCommentsDifferentModulesBasedStatus")
    public List<CommentsDifferentVo> queryCommentsDifferentModulesBasedStatus(Paging paging,Integer userId){

        List<CommentsDifferentVo> commentsDifferentVos = iMyService.queryCommentsDifferentModulesBasedStatus(paging, userId);

        return commentsDifferentVos;
    }


    @ApiOperation(value = "根据状态查询不同模块的收藏", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryFavoritesDifferentModulesAccordingStatus")
    public Object queryFavoritesDifferentModulesAccordingStatus(Paging paging,Integer status,Integer userId){
        return iMyService.queryFavoritesDifferentModulesAccordingStatus(paging,status,userId);
    }

    @ApiOperation(value ="查询我近一个月浏览过的货源，合作，圈子帖子", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryCheckPostsBeenReadingPastMonth")
    public List<CircleClassificationVo> queryCheckPostsBeenReadingPastMonth(int userId, int tagsOne, Paging paging) {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iMyService.queryCheckPostsBeenReadingPastMonth(userId,tagsOne,paging);
    }





}
