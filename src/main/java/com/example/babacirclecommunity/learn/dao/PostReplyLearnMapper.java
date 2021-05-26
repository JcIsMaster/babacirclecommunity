package com.example.babacirclecommunity.learn.dao;

import com.example.babacirclecommunity.learn.entity.LearnPostReply;
import com.example.babacirclecommunity.learn.vo.LearnPostReplyVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author JC
 * @date 2021/4/21 9:15
 */
@Component
public interface PostReplyLearnMapper {

    /**
     * 添加二级评论
     * @param postReply 二级评论对象
     * @return
     */
    @Insert("insert into tb_post_reply_learn(c_id,h_id,bh_id,h_image,h_content,h_voice,create_at,reply_give_status)values" +
            "(${postReply.cId},${postReply.hId},${postReply.bhId},#{postReply.hImage},#{postReply.hContent},#{postReply.hVoice},#{postReply.createAt},${postReply.replyGiveStatus})")
    int addSecondLevelComment(@Param("postReply") LearnPostReply postReply);

    /**
     * 根据一级评论id查询二级评论id
     * @param cId 一级评论id
     * @return
     */
    @Select("select a.id,a.bh_id,a.h_content,a.reply_give_status,b.avatar,b.id as userId,b.user_name from tb_post_reply_learn a INNER JOIN tb_user b on a.h_id=b.id where c_id=${cId} ORDER BY a.create_at desc")
    List<LearnPostReplyVo> queryPostReplyComment(@Param("cId") int cId);
}
