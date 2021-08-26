package com.example.babacirclecommunity.circle.controller;

import com.example.babacirclecommunity.circle.entity.Comment;
import com.example.babacirclecommunity.circle.entity.CommentGive;
import com.example.babacirclecommunity.circle.entity.PostReply;
import com.example.babacirclecommunity.circle.service.ICommentService;
import com.example.babacirclecommunity.circle.vo.CommentReplyVo;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * @author MQ
 * @date 2021/3/4 14:50
 */
@Api(tags = "评论API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/CommentController")
public class CommentController {

    @Autowired
    private ICommentService iCommentService;

    /**
     *
     * 添加评论
     * @return
     */
    @ApiOperation(value = "添加评论",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addComment")
    public int addComment(Comment comment) throws ParseException {
        if(comment.getPId()==0 || comment.getBId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return  iCommentService.addComment(comment);
    }

    /**
     *
     * 添加二级评论
     * @return
     */
    @ApiOperation(value = "添加二级评论",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addSecondLevelComment")
    public int addSecondLevelComment(PostReply postReply) throws ParseException {
        if(postReply.getBhId()==0 || postReply.getHId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return  iCommentService.addSecondLevelComment(postReply);
    }

    /**
     *
     * 根据帖子id查询评论
     * @return
     */
    @ApiOperation(value = "根据帖子id查询评论",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryComments")
    public List<CommentReplyVo> queryComments(int tId, int userId){
        if(tId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return  iCommentService.queryComments(tId,userId);
    }


    /**
     *
     * 评论点赞
     * @return
     */
    @ApiOperation(value = "评论点赞",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addCommentGive")
    public int addCommentGive(CommentGive commentGive){
        if(commentGive.getDId()==0 || commentGive.getBdId()==0 || commentGive.getType()>1){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return  iCommentService.addCommentGive(commentGive);
    }

    /**
     * 删除评论
     * @param id
     * @return
     */
    @ApiOperation(value = "删除评论",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/deleteComment")
    public int deleteComment(int id){
        if(id == 0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iCommentService.deleteComment(id);
    }
}
