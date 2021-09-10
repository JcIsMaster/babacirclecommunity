package com.example.babacirclecommunity.circle.service.impl;


import com.example.babacirclecommunity.circle.dao.CommentMapper;
import com.example.babacirclecommunity.circle.dao.PostReplyMapper;
import com.example.babacirclecommunity.circle.entity.Comment;
import com.example.babacirclecommunity.circle.entity.CommentGive;
import com.example.babacirclecommunity.circle.entity.PostReply;
import com.example.babacirclecommunity.circle.service.ICommentService;
import com.example.babacirclecommunity.circle.vo.CommentReplyVo;
import com.example.babacirclecommunity.circle.vo.PostReplyVo;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.ConstantUtil;
import com.example.babacirclecommunity.common.utils.GoEasyConfig;
import com.example.babacirclecommunity.inform.dao.InformMapper;
import com.example.babacirclecommunity.inform.entity.Inform;
import com.example.babacirclecommunity.user.dao.UserMapper;
import io.goeasy.GoEasy;
import io.goeasy.publish.GoEasyError;
import io.goeasy.publish.PublishListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

/**
 * @author MQ
 * @date 2021/3/4 14:51
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private PostReplyMapper postReplyMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InformMapper informMapper;

    @Override
    public int addComment(Comment comment) throws ParseException {

        //获取token
        String token = ConstantUtil.getToken();
        String identifyTextContent = ConstantUtil.identifyText(comment.getCommentContent(), token);
        if ("87014".equals(identifyTextContent)) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "内容违规");
        }

        comment.setCreateAt(System.currentTimeMillis() / 1000 + "");
        comment.setGiveStatus(0);



        //添加评论
        int i = commentMapper.addComment(comment);
        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"评论失败");
        }

        //评论人id不等于被评论人id时添加和发送消息
        if(comment.getPId() != comment.getBId()){
            //通知对象
            Inform inform=new Inform();
            inform.setContent(comment.getCommentContent());
            inform.setCreateAt(System.currentTimeMillis()/1000+"");
            inform.setOneType(0);
            inform.setTId(comment.getTId());
            inform.setInformType(0);
            inform.setNotifiedPartyId(comment.getBId());
            inform.setNotifierId(comment.getPId());

            //添加评论通知
            int i1 = informMapper.addCommentInform(inform);
            if(i1<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"评论失败");
            }

            //发送消息通知
            GoEasyConfig.goEasy("channel"+comment.getBId(),"0");
            log.info("{}","消息通知成功");
        }


        return i;
    }

    @Override
    public int addSecondLevelComment(PostReply postReply) throws ParseException {
        postReply.setCreateAt(System.currentTimeMillis() / 1000 + "");
        postReply.setReplyGiveStatus(0);

        //获取token
        String token = ConstantUtil.getToken();
        String identifyTextContent = ConstantUtil.identifyText(postReply.getHContent(), token);
        if ("87014".equals(identifyTextContent)) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "内容违规");
        }

        //通知对象
/*        Inform inform=new Inform();
        inform.setContent(postReply.getHContent());
        inform.setCreateAt(System.currentTimeMillis()/1000+"");
        inform.setOneType(0);
        inform.setTId(postReply.get);
        inform.setInformType(0);
        inform.setNotifiedPartyId(postReply.getBId());
        inform.setNotifierId(postReply.getPId());*/

        //添加二级评论
        int i = postReplyMapper.addSecondLevelComment(postReply);
        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }


        return i;
    }

    @Override
    public List<CommentReplyVo> queryComments(int tId, int userId) {
        //查询一级评论
        List<CommentReplyVo> commentReplyVos = commentMapper.queryComment(tId);
        //根据一级评论id查询二级评论
        for (CommentReplyVo s : commentReplyVos) {
            //得到评论点赞数量
//            int i = commentMapper.queryCommentGiveNum(s.getId(), 0);
//            s.setCommentGiveNum(i);


            //查看一级评论是否点赞
//            if (userId != 0) {
//                //是否点赞
//                CommentGive commentGive = commentMapper.queryWhetherGives(userId, 0, s.getId());
//                if (commentGive != null) {
//                    s.setCommentGiveStatus(1);
//                }
//            }


            //根据一级评论id查询二级的评论
            List<PostReplyVo> postReplies = postReplyMapper.queryPostReplyComment(s.getId());

            //得到每个一级评论下面的二级评论数量
//            s.setCommentSize(postReplies.size());

//            for (PostReplyVo a : postReplies) {
//                //得到二级评论评论点赞数量
//                int i1 = commentMapper.queryCommentGiveNum(a.getId(), 1);
//                a.setTwoCommentGiveNum(i1);
//
//                //查看二级评论是否点赞
//                if (userId != 0) {
//                    //是否点赞
//                    CommentGive commentGive = commentMapper.queryWhetherGives(userId, 1, a.getId());
//                    if (commentGive != null) {
//                        a.setTwoCommentGiveStatus(1);
//                    }
//                }
//
//                String userName = userMapper.selectUserById(a.getBhId()).getUserName();
//                if (userName == null) {
//                    throw new ApplicationException(CodeType.SERVICE_ERROR);
//                }
//
//                a.setUName(userName);
//            }

            s.setPostReplyList(postReplies);
        }
        return commentReplyVos;
    }

    @Override
    public int addCommentGive(CommentGive commentGive) {

        CommentGive commentGive1 = null;

        int i = 0;

        commentGive.setCreateAt(System.currentTimeMillis() / 1000 + "");
        //添加一级评论点赞信息
        if (commentGive.getType() == 0) {
            commentGive.setType(0);
            commentGive1 = commentMapper.queryWhetherGive(commentGive.getDId(), 0, commentGive.getCommentId());

            if (commentGive1 != null) {
                //如果等于1就是点赞的状态 在进去就是取消点赞 状态改为0
                if (commentGive1.getGiveStatus() == 1) {
                    i = commentMapper.updateCommentGiveStatus(0, commentGive.getDId(), commentGive.getCommentId(), 0);
                    if (i <= 0) {
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }
                }

                if (commentGive1.getGiveStatus() == 0) {
                    i = commentMapper.updateCommentGiveStatus(1, commentGive.getDId(), commentGive.getCommentId(), 0);
                    if (i <= 0) {
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }
                }
            } else {
                i = commentMapper.addCommentGive(commentGive);
                if (i <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "评论失败");
                }
            }

        }

        //添加二级评论信息
        if (commentGive.getType() == 1) {
            commentGive.setType(1);

            commentGive1 = commentMapper.queryWhetherGive(commentGive.getDId(), 1, commentGive.getCommentId());

            if (commentGive1 != null) {
                //如果等于1就是点赞的状态 在进去就是取消点赞 状态改为0
                if (commentGive1.getGiveStatus() == 1) {
                    i = commentMapper.updateCommentGiveStatus(0, commentGive.getDId(), commentGive.getCommentId(), 1);
                    if (i <= 0) {
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }
                }

                if (commentGive1.getGiveStatus() == 0) {
                    i = commentMapper.updateCommentGiveStatus(1, commentGive.getDId(), commentGive.getCommentId(), 1);
                    if (i <= 0) {
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }
                }
            } else {
                i = commentMapper.addCommentGive(commentGive);
                if (i <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "评论失败");
                }
            }
        }


        return i;
    }

    @Override
    public int deleteComment(int id) {
        int i = commentMapper.deleteComment(id);
        if (i < 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"删除评论失败");
        }
        return i;
    }
}
