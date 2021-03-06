package com.example.babacirclecommunity.inform.dao;

import com.example.babacirclecommunity.inform.entity.Inform;
import com.example.babacirclecommunity.inform.vo.InformUserVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/22 10:39
 */
@Component
public interface InformMapper {

    /**
     * 查询评论通知(圈子)
     * @param userId 当前用户id
     * @param type 0评论，1获赞
     * @param paging 分页
     * @return
     */
    @Select("select a.read_unread,a.id,a.content,a.create_at,a.t_id,b.id as userId,b.user_name,b.avatar,c.content as title,a.one_type,c.is_delete " +
            "from tb_inform a INNER JOIN tb_circles c on a.t_id=c.id INNER JOIN tb_user b on a.notifier_id=b.id " +
            "where a.is_delete=1 and a.inform_type=${type} and a.notified_party_id=${userId} and a.one_type=0 order by a.create_at desc ${paging}")
    List<InformUserVo> queryCommentsNoticeCircle(@Param("userId") int userId, @Param("type") int type, @Param("paging") String paging);

    /**
     * 查询评论通知(提问)
     * @param userId 当前用户id
     * @param type 0评论，1获赞
     * @param paging 分页
     * @return
     */
    @Select("select a.read_unread,a.id,a.content,a.create_at,a.t_id,b.id as userId,b.user_name,b.avatar,c.title,c.description,a.one_type,c.is_delete " +
            "from tb_inform a INNER JOIN tb_question c on a.t_id=c.id INNER JOIN tb_user b on a.notifier_id=b.id " +
            "where a.is_delete=1 and a.inform_type=${type} and a.notified_party_id=${userId} and a.one_type=1 order by a.create_at desc ${paging}")
    List<InformUserVo> queryCommentsNoticeQuestion(@Param("userId") int userId, @Param("type") int type, @Param("paging") String paging);

    /**
     * 查询评论通知(干货)
     * @param userId 当前用户id
     * @param type 0评论，1获赞
     * @param paging 分页
     * @return
     */
    @Select("select a.read_unread,a.id,a.content,a.create_at,a.t_id,b.id as userId,b.user_name,b.avatar,c.title,a.one_type,c.is_delete " +
            "from tb_inform a INNER JOIN tb_dry_goods c on a.t_id=c.id INNER JOIN tb_user b on a.notifier_id=b.id " +
            "where a.is_delete=1 and a.inform_type=${type} and a.notified_party_id=${userId}  and a.one_type=2 order by a.create_at desc ${paging}")
    List<InformUserVo> queryCommentsNoticeDryGoods(@Param("userId") int userId, @Param("type") int type, @Param("paging") String paging);


    /**
     * 查询评论通知(点赞)
     * @param userId 当前用户id
     * @param type 0评论，1获赞
     * @param paging 分页
     * @return
     */
    @Select("select a.read_unread,a.id,a.content,a.create_at,a.t_id,b.id as userId,b.user_name,b.avatar,c.content as title,a.one_type,c.is_delete " +
            "from tb_inform a INNER JOIN tb_circles c on a.t_id=c.id INNER JOIN tb_user b on a.notifier_id=b.id " +
            "where a.is_delete=1 and a.inform_type=${type} and a.notified_party_id=${userId} and a.one_type=0 order by a.create_at desc ${paging}")
    List<InformUserVo> queryCommentsNoticeCircleGive(@Param("userId") int userId, @Param("type") int type, @Param("paging") String paging);

    /**
     * 查询评论通知(点赞)
     * @param userId 当前用户id
     * @param type 0评论，1获赞
     * @param paging 分页
     * @return
     */
    @Select("select a.read_unread,a.id,a.content,a.create_at,a.t_id,b.id as userId,b.user_name,b.avatar,c.title,c.description,a.one_type,c.is_delete " +
            "from tb_inform a INNER JOIN tb_question c on a.t_id=c.id INNER JOIN tb_user b on a.notifier_id=b.id " +
            "where a.is_delete=1 and a.inform_type=${type} and a.notified_party_id=${userId} and a.one_type=1 order by a.create_at desc ${paging}")
    List<InformUserVo> queryCommentsNoticeQuestionGive(@Param("userId") int userId, @Param("type") int type, @Param("paging") String paging);

    /**
     * 查询评论通知(点赞)
     * @param userId 当前用户id
     * @param type 0评论，1获赞
     * @param paging 分页
     * @return
     */
    @Select("select a.read_unread,a.id,a.content,a.create_at,a.t_id,b.id as userId,b.user_name,b.avatar,c.title,a.one_type,c.is_delete " +
            "from tb_inform a INNER JOIN tb_dry_goods c on a.t_id=c.id INNER JOIN tb_user b on a.notifier_id=b.id " +
            "where a.is_delete=1 and a.inform_type=${type} and a.notified_party_id=${userId} and a.one_type=2 order by a.create_at desc ${paging}")
    List<InformUserVo> queryCommentsNoticeDryGoodsGive(@Param("userId") int userId, @Param("type") int type, @Param("paging") String paging);

    /**
     * 添加通知信息
     * @param inform
     * @return
     */
    @Insert("insert into tb_inform(content,notifier_id,notified_party_id,t_id,inform_type,one_type,create_at)values" +
            "(#{inform.content},${inform.notifierId},${inform.notifiedPartyId},${inform.tId},${inform.informType},${inform.oneType},${inform.createAt})")
    int addCommentInform(@Param("inform")Inform inform);

    /**
     * 修改消息状态为已读
     * @param id 消息id
     * @return
     */
    @Update("update tb_inform set read_unread=1 where id=${id}")
    int modifyMessageState(@Param("id") int id);

    /**
     * 查询评论,和点赞消息未读数量
     * @param userId 用户id
     * @param informType（0评论我，1获赞）
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_inform where read_unread=0 and notified_party_id=${userId} and inform_type=${informType}")
    int queryNumberUnreadMessagesBasedUserId(@Param("userId") int userId,@Param("informType") int informType);


}
