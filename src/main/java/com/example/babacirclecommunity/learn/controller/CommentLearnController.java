package com.example.babacirclecommunity.learn.controller;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.learn.entity.LearnComment;
import com.example.babacirclecommunity.learn.entity.LearnCommentGive;
import com.example.babacirclecommunity.learn.entity.LearnPostReply;
import com.example.babacirclecommunity.learn.service.ICommentLearnService;
import com.example.babacirclecommunity.learn.vo.LearnCommentReplyVo;
import com.example.babacirclecommunity.learn.vo.LearnCommentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * @author JC
 * @date 2021/4/20 17:25
 */
@Api(tags = "学习板块评论API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/CommentLearnController")
public class CommentLearnController {

    @Autowired
    private ICommentLearnService iCommentLearnService;

    /**
     * 添加评论
     * @param learnComment
     * @return
     */
    @ApiOperation(value = "添加评论",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addComment")
    public int addComment(LearnComment learnComment) throws ParseException {
        if(learnComment.getPId()==0 || learnComment.getBId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iCommentLearnService.addComment(learnComment);
    }

    /**
     *
     * 添加二级评论
     * @return
     */
    @ApiOperation(value = "添加二级评论",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addSecondLevelComment")
    public int addSecondLevelComment(LearnPostReply postReply) throws ParseException {
        if(postReply.getBhId()==0 || postReply.getHId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iCommentLearnService.addSecondLevelComment(postReply);
    }

    /**
     *
     * 根据帖子id查询评论
     * @return
     */
    @ApiOperation(value = "根据帖子id查询评论",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryComments")
    public List<LearnCommentReplyVo> queryComments(int tId, int userId, int tType){
        if(tId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iCommentLearnService.queryComments(tId,userId,tType);
    }

    /**
     *
     * 根据提问帖子id查询回答列表
     * @return
     */
    @ApiOperation(value = "根据提问帖子id查询回答列表",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryQuestionAskList")
    public ResultUtil queryQuestionAskList(int tId, int userId, int tType, Paging paging){
        if(tId == 0 || paging.getPage() == 0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iCommentLearnService.queryQuestionAskList(tId,userId,tType,paging);
    }


    /**
     *
     * 评论点赞
     * @return
     */
    @ApiOperation(value = "评论点赞",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addCommentGive")
    public int addCommentGive(LearnCommentGive commentGive){
        if(commentGive.getDId()==0 || commentGive.getBdId()==0 || commentGive.getType()>1){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return  iCommentLearnService.addCommentGive(commentGive);
    }

    /**
     * 我的回答列表
     * @return
     */

}
