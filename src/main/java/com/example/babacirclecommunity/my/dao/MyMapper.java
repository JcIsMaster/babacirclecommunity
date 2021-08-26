package com.example.babacirclecommunity.my.dao;

import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.learn.vo.DryGoodsVo;
import com.example.babacirclecommunity.learn.vo.PublicClassTagVo;
import com.example.babacirclecommunity.learn.vo.QuestionVo;
import com.example.babacirclecommunity.my.entity.ComplaintsSuggestions;
import com.example.babacirclecommunity.my.vo.CommentsDifferentVo;
import com.example.babacirclecommunity.my.vo.PeopleCareAboutVo;
import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
import com.example.babacirclecommunity.user.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/6/8 11:15
 */
@Component
public interface MyMapper {

    /**
     * 根据用户id查询关注的人
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select b.id,b.user_name,b.avatar,b.introduce from tb_user_attention a INNER JOIN tb_user b on a.bg_id=b.id where a.gu_id=${userId} and a.is_delete=1 ${paging}")
    List<PeopleCareAboutVo> queryPeopleCareAbout(@Param("userId") int userId,@Param("paging") String paging);

    /**
     * 根据用户id查询关注的人数量
     * @param userId
     * @return
     */
    @Select("select count(b.id) from tb_user_attention a INNER JOIN tb_user b on a.bg_id=b.id where a.gu_id=${userId} and a.is_delete=1")
    Integer queryCareAboutCountByUserId(@Param("userId") int userId);

    /**
     * 根据用户id查询我的粉丝
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select b.id,b.user_name,b.avatar,b.introduce from tb_user_attention a INNER JOIN tb_user b on a.gu_id=b.id where a.bg_id=${userId} and a.is_delete=1 ${paging}")
    List<PeopleCareAboutVo> queryFan(@Param("userId") int userId,@Param("paging") String paging);

    /**
     * 根据用户id查询我的粉丝数量
     * @param userId
     * @return
     */
    @Select("select count(b.id) from tb_user_attention a INNER JOIN tb_user b on a.gu_id=b.id where a.bg_id=${userId} and a.is_delete=1")
    Integer queryFanCountByUserId(@Param("userId") int userId);

    /**
     * 添加投诉与建议新
     * @param complaintsSuggestions 投诉与建议新对象
     * @return
     */
    @Insert("insert into tb_complaints_suggestions(content,user_id,create_at)values(#{complaintsSuggestions.content},${complaintsSuggestions.userId},#{complaintsSuggestions.createAt})")
    int addComplaintsSuggestions(@Param("complaintsSuggestions") ComplaintsSuggestions complaintsSuggestions );

    /**
     * 添加观看记录
     * @param bUserId 被观看人id
     * @param userId 观看人id
     * @param createAt 创建时间
     * @return
     */
    @Insert("insert into tb_viewing_record(viewers_id,beholder_id,create_at)values(${userId},${bUserId},#{createAt})")
    int addViewingRecord(@Param("bUserId") int bUserId,@Param("userId") int userId,@Param("createAt") String createAt);

    /**
     * 查询观看我的人
     * @param userId 被观看人id
     * @param paging 分页
     * @return
     */
    @Select("select b.id,b.avatar,b.user_name,b.introduce,a.create_at from (select DISTINCT * from tb_viewing_record  ORDER BY create_at desc) a INNER JOIN tb_user b on a.viewers_id=b.id where  a.beholder_id=${userId} GROUP BY a.viewers_id ORDER BY a.create_at desc ${paging}")
    List<User> queryPeopleWhoHaveSeenMe(@Param("userId") int userId, @Param("paging") String paging);

    /**
     * 修改用户信息
     * @param user 用户
     * @return
     */
    @Update("update tb_user set user_name=#{user.userName},user_sex=${user.userSex},birthday=#{user.birthday},curr_province=#{user.currProvince},city=#{user.city},county=#{user.county},introduce=#{user.introduce},picture=#{user.picture},avatar=#{user.avatar} where id=${user.id}")
    int updateUserMessage(@Param("user") User user);

    /**
     * 今天有多少人看过我（今天）
     * @param userId 被观看人id
     * @return
     */
    @Select("select COALESCE(count(*),0) from (select DISTINCT * from tb_viewing_record  ORDER BY create_at desc) a INNER JOIN tb_user b on a.viewers_id=b.id where to_days(FROM_UNIXTIME(a.create_at)) = to_days(now()) and  a.beholder_id=${userId} ")
    int queryPeopleWhoHaveSeenMeAvatar(@Param("userId") int userId);

    /**
     * 查询观看时间
     * @param viewersId
     * @param beholderId
     * @return
     */
    @Select("select create_at from tb_viewing_record where viewers_id=${viewersId} and beholder_id=${beholderId} order by create_at desc")
    String[] queryCreateAt(@Param("viewersId") int viewersId,@Param("beholderId") int beholderId);

    /**
     * 根据用户id查询评论过的圈子帖子
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select b.type,b.id,a.comment_content,b.cover,b.content,a.create_at,b.is_delete,IFNULL(NULL, 0) AS type_name from tb_comment a INNER JOIN tb_circles b on a.t_id=b.id where  a.p_id=${userId} and a.is_delete=1 ORDER BY a.create_at desc ${paging}")
    List<CommentsDifferentVo> queryCommentsDifferentCircle(@Param("userId") int userId,@Param("paging") String paging);

    /**
     * 根据用户id查询评论过的干货帖子
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select b.id, a.comment_content,b.cover_img as cover,b.title as content,a.create_at,b.is_delete,IFNULL(NULL, 1) AS type_name from tb_learn_comment a INNER JOIN tb_dry_goods b on a.t_id=b.id where  a.p_id=${userId} and a.is_delete=1 and a.t_type=1 ORDER BY a.create_at desc ${paging}")
    List<CommentsDifferentVo> queryCommentsDifferentDryGoods(@Param("userId") int userId,@Param("paging") String paging);

    /**
     * 根据用户id查询评论过的提问帖子
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select b.id,a.comment_content,b.cover_img as cover,b.title as content,a.create_at,b.is_delete,IFNULL(NULL, 2) AS type_name from tb_learn_comment a INNER JOIN tb_question b on a.t_id=b.id where  a.p_id=${userId} and a.is_delete=1 and a.t_type=0 ORDER BY a.create_at desc ${paging}")
    List<CommentsDifferentVo> queryCommentsDifferentQuestion(@Param("userId") int userId,@Param("paging") String paging);

    /**
     * 查询我收藏的货源帖子
     * @param userId 用户id
     * @param status 状态 0 查货源收藏，1查合作收藏
     * @param paging 分页
     * @return
     */
    @Select("select a.id,d.id as uId,d.user_name,d.avatar,a.title,a.browse,a.type,a.video,a.cover,b.tag_name,b.id as tagId " +
            "from tb_resources a INNER JOIN tb_user d on a.u_id=d.id INNER JOIN tb_tags b on a.tags_two=b.id INNER JOIN tb_user_collection c on a.id=c.t_id  " +
            "where c.u_id=${userId} and a.is_delete=1 and a.tags_one=12 and c.is_delete=1 and c.type_collection=${status} order by c.create_at desc ${paging}")
    List<ResourceClassificationVo> queryFavoritePosts(@Param("userId") int userId,@Param("status") int status, @Param("paging") String paging);

    /**
     * 查询我收藏的合作帖子
     * @param userId 用户id
     * @param status 状态 0 查货源收藏，1查合作收藏
     * @param paging 分页
     * @return
     */
    @Select("select a.id,d.id as uId,d.user_name,d.avatar,a.title,a.browse,a.type,a.video,a.cover,b.tag_name,b.id as tagId " +
            "from tb_resources a INNER JOIN tb_user d on a.u_id=d.id INNER JOIN tb_tags b on a.tags_two=b.id INNER JOIN tb_user_collection c on a.id=c.t_id  " +
            "where c.u_id=${userId} and a.is_delete=1 and a.tags_one=13 and c.is_delete=1 and c.type_collection=${status} order by c.create_at desc ${paging}")
    List<ResourceClassificationVo> queryFavoritePostsCollaborate(@Param("userId") int userId,@Param("status") int status, @Param("paging") String paging);

    /**
     * 查询收藏的干货
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select a.id,a.title,d.user_name,d.avatar,a.cover_img,c.create_at,a.description,a.u_id" +
            " from tb_dry_goods a INNER JOIN tb_user d on a.u_id=d.id INNER JOIN tb_learn_collect c on a.id=c.zq_id " +
            " where c.u_id=${userId} and a.is_delete=1 and c.give_cancel=1 and c.learn_type=1 order by c.create_at desc ${paging}")
    List<DryGoodsVo> queryCollectDry(@Param("userId") int userId,@Param("paging") String paging);

    /**
     * 查询收藏的提问
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select a.id,a.title,d.user_name,d.avatar,a.cover_img,c.create_at,b.tag_name,a.favour,a.collect,a.comment,a.description" +
            " from tb_question a INNER JOIN tb_user d on a.u_id=d.id INNER JOIN tb_tags b on a.tags_two=b.id INNER JOIN tb_learn_collect c on a.id=c.zq_id " +
            " where c.u_id=${userId} and a.is_delete=1 and c.give_cancel=1 and c.learn_type=0 order by c.create_at desc ${paging}")
    List<QuestionVo> queryCollectQuestion(@Param("userId") int userId,@Param("paging") String paging);

    /**
     * 查询收藏的公开课
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select a.id,a.title,d.user_name,d.avatar,a.cover_img,c.create_at,b.tag_name,a.collect,a.price,a.buyer_num" +
            " from tb_public_class a INNER JOIN tb_user d on a.u_id=d.id INNER JOIN tb_tags b on a.tags_two=b.id INNER JOIN tb_learn_collect c on a.id=c.zq_id" +
            " where c.u_id=${userId} and a.is_delete=1 and c.give_cancel=1 and c.learn_type=2 order by c.create_at desc ${paging}")
    List<PublicClassTagVo> queryCollectPublicClass(@Param("userId") int userId,@Param("paging") String paging);

    /**
     * 查询我近一个月浏览的圈子记录
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select b.id,b.content,c.id as uId,b.cover,c.avatar,c.user_name ,a.create_at,b.type from tb_browse a " +
            "INNER JOIN tb_circles b on a.zq_id=b.id INNER JOIN tb_user c on b.user_id=c.id " +
            "INNER JOIN tb_tags d on b.tags_two=d.id where UNIX_TIMESTAMP(DATE_SUB(FROM_UNIXTIME(unix_timestamp(now()),'%Y-%m-%d %H:%i:%s'), INTERVAL 30 DAY))<=a.create_at " +
            "and a.u_id=${userId} and b.is_delete=1 and a.type=1 ORDER BY a.create_at desc ${paging}")
    List<CircleClassificationVo> queryCheckPostsBeenReadingPastMonth(@Param("userId")int userId, @Param("paging") String paging);

    /**
     * 查询我近一个月浏览过的货源和合作记录
     * @param userId 用户id
     * @param tagsOne 12货源  13合作
     * @param paging 分页
     * @return
     */
    @Select("select b.id,b.title as content,c.id as uId,b.cover,c.avatar,c.user_name,a.create_at,b.type from tb_browse a " +
            "INNER JOIN tb_resources b on a.zq_id=b.id INNER JOIN tb_user c on b.u_id=c.id " +
            "INNER JOIN tb_tags d on b.tags_two=d.id where UNIX_TIMESTAMP(DATE_SUB(FROM_UNIXTIME(unix_timestamp(now()),'%Y-%m-%d %H:%i:%s'), INTERVAL 30 DAY))<=a.create_at " +
            "and a.u_id=${userId} and b.is_delete=1 and b.tags_one=${tagsOne} and a.type=0  ORDER BY a.create_at desc ${paging}")
    List<CircleClassificationVo> queryCheckPostsBeenReadingPastMonthResource(@Param("userId")int userId,@Param("tagsOne") int tagsOne, @Param("paging") String paging);

    /**
     * 查询我近一个月浏览记录数量
     * @param userId 用户id
     * @return
     */
    @Select("select count(*) from tb_browse a " +
            "INNER JOIN tb_circles b on a.zq_id=b.id INNER JOIN tb_user c on b.user_id=c.id " +
            "INNER JOIN tb_tags d on b.tags_two=d.id where UNIX_TIMESTAMP(DATE_SUB(FROM_UNIXTIME(unix_timestamp(now()),'%Y-%m-%d %H:%i:%s'), INTERVAL 30 DAY))<=a.create_at " +
            "and a.u_id=${userId} and b.is_delete=1 and a.type=1 ORDER BY a.create_at desc")
    int queryCheckPostsBeenReadingPastMonthCount(@Param("userId")int userId);

}
