package com.example.babacirclecommunity.learn.service;

import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.learn.entity.LearnComment;
import com.example.babacirclecommunity.learn.entity.LearnCommentGive;
import com.example.babacirclecommunity.learn.entity.LearnPostReply;
import com.example.babacirclecommunity.learn.vo.LearnCommentReplyVo;
import com.example.babacirclecommunity.learn.vo.LearnCommentVo;

import java.text.ParseException;
import java.util.List;

/**
 * @author JC
 * @date 2021/4/21 9:23
 */
public interface ICommentLearnService {

    /**
     * 添加评论
     * @param comment 评论对象
     * @throws ParseException
     * @return
     */
    int addComment(LearnComment comment) throws ParseException;

    /**
     * 添加二级评论
     * @param postReply 二级评论对象
     * @throws ParseException
     * @return
     */
    int addSecondLevelComment(LearnPostReply postReply) throws ParseException;

    /**
     * 查询评论
     * @param tId 帖子id
     * @param userId 用户id
     * @param tType 帖子类型
     * @return
     */
    List<LearnCommentReplyVo> queryComments(int tId, int userId, int tType);

    /**
     * 根据提问帖子id查询回答列表
     * @param tId 帖子id
     * @param userId 用户id
     * @param tType 帖子类型
     * @param paging 分页
     * @return
     */
    ResultUtil queryQuestionAskList(int tId, int userId, int tType, Paging paging);

    /**
     * 评论点赞
     * @param commentGive 评论点赞对象
     * @return
     */
    int addCommentGive(LearnCommentGive commentGive);
}
