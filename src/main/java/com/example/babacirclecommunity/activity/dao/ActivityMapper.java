package com.example.babacirclecommunity.activity.dao;

import com.example.babacirclecommunity.activity.entity.Activity;
import com.example.babacirclecommunity.activity.entity.ActivityParticipate;
import com.example.babacirclecommunity.activity.vo.ActivityListVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author JC
 * @date 2021/9/15 15:11
 */
@Component
public interface ActivityMapper {

    /**
     * 查询活动列表
     * @param area
     * @param sql
     * @return
     */
    @Select("<script>"+
            "select a.id,a.activity_title,a.activity_cover,a.sponsor_user_id,a.activity_end_time,b.user_name as sponsorUserName,b.avatar " +
            "as sponsorUserAvatar,count(c.id) as numberOfParticipants from tb_activity a left join tb_user b on a.sponsor_user_id = b.id " +
            "left join tb_activity_participate c on a.id = c.activity_id and c.is_delete = 0 where a.is_delete = 0 " +
            "<if test='area != null'>and a.activity_area LIKE CONCAT('%',#{area},'%')</if>" +
            "GROUP BY a.id ORDER BY a.activity_start_time desc ${sql}" +
            "</script>")
    List<ActivityListVo> queryActivityList(@Param("area") String area, @Param("sql") String sql);

    /**
     * 查询活动详情
     * @param id
     * @return
     */
    @Select("select * from tb_activity where id = ${id}")
    Activity queryActivityDetailsById(@Param("id") int id);

    /**
     * 查看用户是否参与该活动
     * @param id
     * @param userId
     * @return
     */
    @Select("select count(*) from tb_activity_participate where activity_id = ${id} and user_id = ${userId}")
    int queryWhetherToParticipateInActivity(@Param("id") int id,@Param("userId") int userId);

    /**
     * 用户参与活动
     * @param activityParticipate
     * @return
     */
    @Insert("INSERT INTO tb_activity_participate(activity_id,user_id,user_name,user_phone_number,user_address,create_at) SELECT " +
            "${activityParticipate.activityId},${activityParticipate.userId},#{activityParticipate.userName},#{activityParticipate.userPhoneNumber}," +
            "#{activityParticipate.userAddress},#{activityParticipate.createAt} FROM DUAL WHERE NOT EXISTS(SELECT * FROM tb_activity_participate " +
            "WHERE activity_id = ${activityParticipate.activityId} and user_id = ${activityParticipate.userId})")
    int participateInActivity(@Param("activityParticipate") ActivityParticipate activityParticipate);

    /**
     * 创建活动
     * @param activity
     * @return
     */
    @Insert("insert into tb_activity(activity_title,activity_cover,activity_content,activity_sponsor,activity_time,activity_area,activity_location," +
            "sponsor_user_id,activity_fee,activity_fee_desc,activity_rule,activity_start_time,activity_end_time) values(#{activity.activityTitle}," +
            "#{activity.activityCover},#{activity.activityContent},#{activity.activitySponsor},#{activity.activityTime},#{activity.activityArea},#{activity.activityLocation}," +
            "${activity.sponsorUserId},${activity.activityFee},#{activity.activityFeeDesc},#{activity.activityRule},#{activity.activityStartTime}," +
            "#{activity.activityEndTime})")
    @Options(useGeneratedKeys=true, keyProperty="activity.id",keyColumn="id")
    int createActivity(@Param("activity") Activity activity);

    /**
     * 查询未截止（进行中）的活动
     * @return
     */
    @Select("select * from tb_activity where is_delete = 0")
    List<Activity> queryNotDueActivity();

    /**
     * 根据活动id截止活动报名
     * @param id
     * @return
     */
    @Update("update tb_activity set is_delete = 1 where id = ${id}")
    int dueActivityById(@Param("id") int id);
}
