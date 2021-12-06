package com.example.babacirclecommunity.activity.dao;

import com.example.babacirclecommunity.activity.entity.ActivityOnline;
import com.example.babacirclecommunity.activity.entity.ActivityOnlineDiscount;
import com.example.babacirclecommunity.activity.entity.ActivityOnlineOrder;
import com.example.babacirclecommunity.activity.entity.ActivityOnlineParticipate;
import com.example.babacirclecommunity.activity.vo.*;
import com.example.babacirclecommunity.common.utils.Paging;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author JC
 * @date 2021/10/6 11:41
 */
@Component
public interface ActivityOnlineMapper {


    /**
     * 查询线上活动列表
     * @param activityLevel
     * @param sql
     * @return
     */
    @Select("select id,cover,title,initiator_user_id,original_price,discount_price from tb_activity_online where activity_level = ${activityLevel} " +
            "and is_status = 0 order by create_at desc ${sql}")
    List<ActivityOnlineListVo> queryActivityOnlineList(@Param("activityLevel") int activityLevel,@Param("sql") String sql);

    /**
     * 查询线上活动详情
     * @param id
     * @return
     */
    @Select("select * from tb_activity_online where id = ${id} and is_status = 0")
    ActivityOnlineVo queryActivityOnlineDetail(@Param("id") int id);

    /**
     * 根据id查询线上活动库存
     * @param id
     * @return
     */
    @Select("select stock from tb_activity_online where id = ${id} and is_status = 0")
    int queryActivityOnlineStock(@Param("id") int id);

    /**
     * 根据活动id查询发布活动的人
     * @param id
     * @return
     */
    @Select("select initiator_user_id from tb_activity_online where id = ${id}")
    Integer queryActivityOnlineInitiatorUserId(@Param("id") int id);

    /**
     * 查询线上活动详情中的活动参与者
     * @param id
     * @return
     */
    @Select("select a.id,a.user_id,b.user_name,b.avatar from tb_activity_online_participate a LEFT JOIN tb_user b on a.user_id = b.id " +
            "where a.activity_online_id = ${id} limit 6")
    List<ActivityOnlineParticipateVo> queryActivityOnlineParticipateVo(@Param("id") int id);

    /**
     * 查询线上活动参与者人数
     * @param id
     * @return
     */
    @Select("select count(*) from tb_activity_online_participate where activity_online_id = ${id}")
    int queryActivityOnlineParticipateCount(@Param("id") int id);

    /**
     * 创建线上活动
     * @param activityOnline
     * @return
     */
    @Insert("insert into tb_activity_online(cover,title,initiator_user_id,original_price,discount_price,stock,finish_time,shop_name,shop_url,shop_phone,we_chat_number," +
            "description,create_at,single_discount_rate,activity_level) values(#{activityOnline.cover},#{activityOnline.title},${activityOnline.initiatorUserId},${activityOnline.originalPrice}," +
            "${activityOnline.discountPrice},${activityOnline.stock},#{activityOnline.finishTime},#{activityOnline.shopName},#{activityOnline.shopUrl},#{activityOnline.shopPhone}," +
            "#{activityOnline.weChatNumber},#{activityOnline.description},#{activityOnline.createAt},${activityOnline.singleDiscountRate},${activityOnline.activityLevel})")
    @Options(useGeneratedKeys=true, keyProperty="activityOnline.id",keyColumn="id")
    int createActivityOnline(@Param("activityOnline")ActivityOnline activityOnline);

    /**
     * 修改线上活动浏览量
     * @param id
     * @return
     */
    @Update("update tb_activity_online set browse = browse + 1 where id = ${id}")
    int updateActivityOnlineBrowse(@Param("id") int id);

    /**
     * 根据用户id、活动id查询用户参与活动详情
     * @param userId
     * @param activityOnlineId
     * @return
     */
    @Select("select * from tb_activity_online_participate where user_id = ${userId} and activity_online_id = ${activityOnlineId}")
    ActivityOnlineParticipate queryActivityOnlineParticipateInfo(@Param("userId") int userId,@Param("activityOnlineId") int activityOnlineId);

    /**
     * 查询部分活动信息
     * @param activityOnlineId
     * @return
     */
    @Select("select a.id,a.cover,a.title,a.initiator_user_id,b.user_name as initiatorUserName,a.original_price,a.discount_price,a.stock,a.finish_time," +
            "b.avatar as initiatorAvatar,a.shop_name from tb_activity_online a inner join tb_user b on a.initiator_user_id = b.id where a.id = ${activityOnlineId}")
    ActivityOnlineJoinVo queryActivityOnlineJoinVo(@Param("activityOnlineId") int activityOnlineId);

    /**
     * 根据活动参与id查询砍价列表
     * @param onlineParticipateId
     * @param sql
     * @return
     */
    @Select("select a.id,a.user_id,b.user_name,b.avatar,a.discount_rate,a.create_at from tb_activity_online_discount a INNER JOIN tb_user b " +
            "on a.user_id = b.id where a.online_participate_id = ${onlineParticipateId} order by a.create_at desc ${sql}")
    List<ActivityOnlineDiscountVo> queryActivityOnlineDiscountVo(@Param("onlineParticipateId") int onlineParticipateId, @Param("sql") String sql);

    /**
     * 根据活动参与id查询当前总优惠
     * @param onlineParticipateId
     * @return
     */
    @Select("select IFNULL(SUM(discount_rate),0) from tb_activity_online_discount where online_participate_id = ${onlineParticipateId}")
    BigDecimal queryTotalDiscountNow(@Param("onlineParticipateId") int onlineParticipateId);

    /**
     * 新增活动参与记录
     * @param activityOnlineParticipate
     * @return
     */
    @Insert("insert into tb_activity_online_participate(user_id,activity_online_id,create_at) values(${activityOnlineParticipate.userId}," +
            "${activityOnlineParticipate.activityOnlineId},#{activityOnlineParticipate.createAt})")
    @Options(useGeneratedKeys=true, keyProperty="activityOnlineParticipate.id",keyColumn="id")
    int addActivityOnlineParticipateInfo(@Param("activityOnlineParticipate") ActivityOnlineParticipate activityOnlineParticipate);

    /**
     * 根据活动参与id和用户id查询帮砍价记录
     * @param onlineParticipateId
     * @param userId
     * @return
     */
    @Select("select * from tb_activity_online_discount where online_participate_id = ${onlineParticipateId} and user_id = ${userId}")
    ActivityOnlineDiscount hasItHelped(@Param("onlineParticipateId") int onlineParticipateId,@Param("userId") int userId);

    /**
     * 帮砍价(助力)
     * @param activityOnlineDiscount
     * @return
     */
    @Insert("insert into tb_activity_online_discount(online_participate_id,user_id,discount_rate,create_at) values(${activityOnlineDiscount.onlineParticipateId}," +
            "${activityOnlineDiscount.userId},${activityOnlineDiscount.discountRate},#{activityOnlineDiscount.createAt})")
    @Options(useGeneratedKeys=true, keyProperty="activityOnlineDiscount.id",keyColumn="id")
    int helpBargain(@Param("activityOnlineDiscount")ActivityOnlineDiscount activityOnlineDiscount);

    /**
     * 活动(助力)详情页
     * @param onlineParticipateId
     * @return
     */
    @Select("select b.id,a.id as activityId,a.cover,a.title,b.user_id,c.user_name,c.avatar,a.original_price,a.discount_price,a.single_discount_rate,b.create_at " +
            "from tb_activity_online a right join tb_activity_online_participate b on a.id = b.activity_online_id LEFT JOIN tb_user c " +
            "on b.user_id = c.id where b.id = ${onlineParticipateId}")
    ActivityOnlineHelpInfoVo queryHelpBargainInfo(@Param("onlineParticipateId") int onlineParticipateId);

    /**
     * 新增在线活动订单记录
     * @param activityOnlineOrder
     * @return
     */
    @Insert("insert into tb_activity_online_order(activity_online_id,user_id,price,create_at) values(${activityOnlineOrder.activityOnlineId}," +
            "${activityOnlineOrder.userId},${activityOnlineOrder.price},#{activityOnlineOrder.createAt})")
    @Options(useGeneratedKeys = true,keyProperty = "activityOnlineOrder.id",keyColumn = "id")
    int addBuyActivityItemsRecording(@Param("activityOnlineOrder") ActivityOnlineOrder activityOnlineOrder);

    /**
     * 查询是否已购买过该活动产品
     * @param activityOnlineId
     * @param userId
     * @return
     */
    @Select("select * from tb_activity_online_order where activity_online_id = ${activityOnlineId} and user_id = ${userId}")
    ActivityOnlineOrder queryWhetherPurchased(@Param("activityOnlineId") int activityOnlineId,@Param("userId") int userId);

    /**
     * 查询进行中的线上活动
     * @return
     */
    @Select("select * from tb_activity_online where is_status = 0")
    List<ActivityOnline> queryNotDueActivityOnline();

    /**
     * 根据活动id结束线上活动
     * @param id
     * @return
     */
    @Update("update tb_activity_online set is_status = 1 where id = ${id}")
    int dueActivityOnlineById(@Param("id") int id);

    /**
     * 减少库存
     * @param id
     * @return
     */
    @Update("update tb_activity_online set stock = stock - 1 where id = ${id}")
    int reduceActivityOnlineStock(@Param("id") int id);

    /**
     * 用户查询活动订单
     * @param userId
     * @param sql
     * @return
     */
    @Select("select a.id,a.activity_online_id,b.title,b.original_price,b.discount_price,b.is_status,a.user_id,c.user_name,c.avatar,a.price,a.create_at " +
            "from tb_activity_online_order a left join tb_activity_online b on a.activity_online_id = b.id left join tb_user c on a.user_id = c.id " +
            "where b.initiator_user_id = ${userId} ${sql}")
    List<ActivityOnlineOrderVo> queryActivityOrdersByUser(@Param("userId") int userId, @Param("sql") String sql);

    /**
     * 查询用户当月已创建线上活动的数量
     * @param userId
     * @return
     */
    @Select("select count(id) from tb_activity_online where initiator_user_id = ${userId} and " +
            "FROM_UNIXTIME(create_at,\"%Y-%m\") = DATE_FORMAT(NOW(),\"%Y-%m\")")
    int queryCreatedActivityOnlineNumFromCurrentMonth(@Param("userId") int userId);

}
