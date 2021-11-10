package com.example.babacirclecommunity.circle.service;


import com.example.babacirclecommunity.circle.entity.Comment;
import com.example.babacirclecommunity.circle.entity.CommentGive;
import com.example.babacirclecommunity.circle.entity.PostReply;
import com.example.babacirclecommunity.circle.vo.CommentReplyVo;

import java.text.ParseException;
import java.util.List;


/**
 * @author MQ
 * @date 2021/3/4 14:50
 */
public interface ICommentService {

    /**
     * 添加评论
     * @param comment
     * @return
     */
    int addComment(Comment comment) throws ParseException;

    /**
     * 添加二级评论
     * @param postReply 二级评论对象
     * @return
     */
    int addSecondLevelComment(PostReply postReply) throws ParseException;

    /**
     * 查询评论
     * @param tId 帖子id
     * @param userId
     * @return
     */
    List<CommentReplyVo> queryComments(int tId, int userId);

    /**
     * 评论点赞
     * @param commentGive
     * @return
     */
    int addCommentGive(CommentGive commentGive);

    /**
     * 删除评论
     * @param id
     * @return
     */
    int deleteComment(int id);
}
