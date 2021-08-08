package com.example.babacirclecommunity.learn.dao;


import com.example.babacirclecommunity.learn.entity.LearnComment;
import com.example.babacirclecommunity.learn.entity.LearnCommentGive;
import com.example.babacirclecommunity.learn.vo.LearnCommentReplyVo;
import com.example.babacirclecommunity.learn.vo.LearnCommentUserVo;
import com.example.babacirclecommunity.learn.vo.LearnCommentVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author JC
 * @date 2021/4/20 17:39
 */
@Component
public interface LearnCommentMapper {

    /**
     * 查看帖子的评论数量
     * @param tid
     * @param tType
     * @return
     */
    @Select("select COALESCE(count(*)) from tb_learn_comment where t_id=${tid} and t_type=${tType} and is_delete = 1")
    Integer selectCommentNumber(@Param("tid") int tid,@Param("tType") int tType);

    /**
     * 查询表面显示的评论
     * @param tid
     * @param tType
     * @return
     */
    @Select("select a.*,b.id as userId,b.user_name from tb_learn_comment a INNER JOIN tb_user b on a.p_id=b.id where a.t_id=${tid} and a.t_type=${tType} and a.is_delete=1 order by a.create_at desc limit 3")
    List<LearnCommentUserVo> selectComment(@Param("tid") int tid, @Param("tType") int tType);

    /**
     * 添加评论
     * @param comment
     * @return
     */
    @Insert("insert into tb_learn_comment(p_id,b_id,t_id,t_type,comment_content,give_status,create_at)values(${comment.pId},${comment.bId},${comment.tId},${comment.tType},#{comment.commentContent},${comment.giveStatus},#{comment.createAt})")
    int addComment(@Param("comment") LearnComment comment);

    /**
     * 根据帖子id查询评论
     * @param tId 帖子id
     * @param tType 帖子类型
     * @return
     */
    @Select("select a.*,b.id as userId,b.user_name,b.avatar from tb_learn_comment a INNER JOIN tb_user b on a.p_id=b.id where a.t_id=${tId} and a.t_type=${tType} and a.is_delete=1 order by a.create_at desc")
    List<LearnCommentReplyVo> queryComment(@Param("tId") int tId, @Param("tType") int tType);

    /**
     * 根据帖子id查询提问回答列表
     * @param tId 帖子id
     * @param tType 帖子类型
     * @param sql 分页
     * @return
     */
    @Select("select a.*,b.id as userId,b.user_name,b.avatar from tb_learn_comment a INNER JOIN tb_user b on a.p_id=b.id where a.t_id=${tId} and a.t_type=${tType} and a.is_delete=1 order by a.create_at desc ${sql}")
    List<LearnCommentVo> queryQuestionAskList(@Param("tId") int tId, @Param("tType") int tType, @Param("sql") String sql);

    /**
     * 增加评论点赞信息
     * @param commentGive 评论点赞对象
     * @return
     */
    @Insert("insert into tb_learn_comment_give(comment_id,d_id,bd_id,create_at,type)values(${commentGive.commentId},${commentGive.dId},${commentGive.bdId},#{commentGive.createAt},${commentGive.type})")
    int addCommentGive(@Param("commentGive") LearnCommentGive commentGive);

    /**
     * 根据评论id查询这条评论的点赞数量
     * @param commentId 评论id
     * @param type 评论类型id 0一级评论 1二级评论
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_learn_comment_give where comment_id=${commentId} and type=${type} and give_status=1 and is_delete=1")
    int queryCommentGiveNum(@Param("commentId") int commentId,@Param("type") int type);

    /**
     * 根据用户id查询 是否点赞过这评论
     * @param dId  点赞人id
     * @param type 评论类型id 0一级评论 1二级评论
     * @param id 评论id
     * @return
     */
    @Select("select * from tb_learn_comment_give where d_id=${dId} and comment_id=${id} and type=${type} and is_delete=1")
    LearnCommentGive queryWhetherGive(@Param("dId") int dId, @Param("type") int type, @Param("id") int id);

    /**
     * 根据用户id查询 是否点赞了这评论
     * @param dId  点赞人id
     * @param type 评论类型id 0一级评论 1二级评论
     * @param id 评论id
     * @return
     */
    @Select("select * from tb_learn_comment_give where d_id=${dId} and comment_id=${id} and type=${type} and give_status=1 and is_delete=1")
    LearnCommentGive queryWhetherGives(@Param("dId") int dId,@Param("type") int type,@Param("id") int id);

    /**
     * 修改评论点赞状态
     * @param giveStatus
     * @param dId 评论人
     * @param commentId 被关注人id
     * @param type 被关注人id
     * @return
     */
    @Update("Update tb_learn_comment_give set give_status=${giveStatus} where d_id=${dId} and comment_id=${commentId} and type=${type}")
    int updateCommentGiveStatus(@Param("giveStatus") int giveStatus,@Param("dId") int dId,@Param("commentId") int commentId,@Param("type") int type);
}
