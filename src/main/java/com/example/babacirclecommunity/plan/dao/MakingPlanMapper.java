package com.example.babacirclecommunity.plan.dao;

import com.example.babacirclecommunity.plan.entity.*;
import com.example.babacirclecommunity.plan.vo.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author JC
 * @date 2021/7/12 13:44
 */
@Component
public interface MakingPlanMapper {

    /**
     * 查询目标参数题目
     * @return
     */
    @Select("select id,topic,remark,create_at,is_delete FROM tb_plan_topic where is_delete = 0")
    List<TopicOptionsVo> getPlanTopic();

    /**
     * 查询目标参数选项
     * @return
     */
    @Select("select id,options,topic_id from tb_plan_options where is_delete = 0")
    List<PlanOptions> getPlanOptions();

    /**
     * 生成（新增）用户计划
     * @param userPlan
     * @return
     */
    @Insert("insert into tb_user_plan(user_id,plan_id,create_at,update_time) value(${userPlan.userId},${userPlan.planId},#{userPlan.createAt},#{userPlan.createAt})")
    @Options(useGeneratedKeys=true, keyProperty="userPlan.id",keyColumn="id")
    int generatePlan(@Param("userPlan") UserPlan userPlan);

    /**
     * 查看用户计划
     * @param userId
     * @return
     */
    @Select("select id,user_id,plan_id,sing_in_record,complete_schedule,create_at from tb_user_plan where user_id = ${userId}")
    UserPlanVo queryUserPlan(@Param("userId") int userId);

    /**
     * 根据目标参数查找计划
     * @param planOptions
     * @return
     */
    @Select("select * from tb_plan where options_array = #{planOptions} and is_delete = 0")
    Plan queryPlanByOptions(@Param("planOptions") String planOptions);

    /**
     * 查询（生成计划页）课程预告列表
     * @param planId
     * @param surplus
     * @return
     */
    @Select("select a.*,count(DISTINCT b.user_id,b.plan_class_id) as students_number from tb_plan_class a left join tb_plan_class_record b on a.id = b.plan_class_id " +
            "where a.plan_id = ${planId} and a.is_delete = 0 and a.date_weight > ${surplus} group by b.plan_class_id order by " +
            "a.date_weight limit 3")
    List<PlanClass> queryClassLearnList(@Param("planId") int planId, @Param("surplus") int surplus);

    /**
     * 查询计划课程详情
     * @param planClassId 计划课程id
     * @return
     */
    @Select("select a.*,b.community_name,(select count(DISTINCT user_id,plan_class_id) from tb_plan_class_record where plan_class_id=${planClassId}) as studentsNumber from tb_plan_class a " +
            "left join tb_community b on a.tag_id = b.tag_id where a.id = ${planClassId} and a.is_delete = 0")
    PlanClassVo queryPlanClassDetail(@Param("planClassId") int planClassId);

    /**
     * 根据课程id查询该课程下的所有视频
     * @param planClassId
     * @return
     */
    @Select("select a.video_id as id,b.video_name,b.video_desc,b.video_cover,b.video_address,b.video_type from tb_class_video a inner join tb_video b " +
            "on a.video_id = b.id where a.plan_class_id = ${planClassId} and b.is_delete = 0 order by a.create_at")
    List<ClassVideo> queryPlanClassVideoList(@Param("planClassId") int planClassId);

    /**
     * 新增计划课程反馈
     * @param planClassFeedback
     * @return
     */
    @Insert("INSERT INTO tb_plan_class_feedback(plan_class_id,scoring,feedback_tag,detail,create_at) VALUES (${planClassFeedback.planClassId}," +
            "${planClassFeedback.scoring},#{planClassFeedback.feedbackTag},#{planClassFeedback.detail},#{planClassFeedback.createAt})")
    @Options(useGeneratedKeys=true, keyProperty="planClassFeedback.id",keyColumn="id")
    int addPlanFeedBack(@Param("planClassFeedback") PlanClassFeedback planClassFeedback);

    /**
     * 根据tagId查询增强计划列表
     * @param tagId
     * @param sql
     * @return
     */
    @Select("select * from tb_plan_class where tag_id = ${tagId} and is_enhance = 1 and is_delete = 0 ${sql}")
    List<PlanClass> queryEnhancePlanListByTag(@Param("tagId") int tagId,@Param("sql") String sql);

    /**
     * 添加课程学习记录
     * @param planClassRecord
     * @return
     */
    @Insert("INSERT INTO tb_plan_class_record(user_id,plan_class_id,video_id,watch_time,total_video_duration,video_viewing_progress,create_at, " +
            "update_time) VALUES (${planClassRecord.userId},${planClassRecord.planClassId},${planClassRecord.videoId},${planClassRecord.watchTime}, " +
            "${planClassRecord.totalVideoDuration},${planClassRecord.videoViewingProgress},#{planClassRecord.createAt},#{planClassRecord.updateTime})")
    @Options(useGeneratedKeys=true, keyProperty="planClassRecord.id",keyColumn="id")
    int addLearningRecord(@Param("planClassRecord") PlanClassRecord planClassRecord);

    /**
     * 查询用户是否已学习过某课程
     * @param userId
     * @param planClassId
     * @param videoId
     * @return
     */
    @Select("select * from tb_plan_class_record where user_id = ${userId} and plan_class_id = ${planClassId} and video_id = ${videoId} and is_delete = 0")
    PlanClassRecord queryLearningRecord(@Param("userId") int userId,@Param("planClassId") int planClassId,@Param("videoId") int videoId);

    /**
     * 修改课程学习记录
     * @param planClassRecord
     * @return
     */
    @Update("update tb_plan_class_record set watch_time = ${planClassRecord.watchTime},video_viewing_progress = ${planClassRecord.videoViewingProgress}, " +
            "difference_from_last_time = ${planClassRecord.differenceFromLastTime},update_time = #{planClassRecord.updateTime} where " +
            "id = ${planClassRecord.id}")
    int updateLearningRecord(@Param("planClassRecord") PlanClassRecord planClassRecord);

    /**
     * 根据用户id查询该用户最近学习课程（3条）
     * @param userId
     * @return
     */
    @Select("select a.*,sum(b.video_viewing_progress) as videoViewingProgress from tb_plan_class a RIGHT JOIN tb_plan_class_record b on a.id = b.plan_class_id where " +
            "b.user_id = ${userId} and a.is_delete = 0 GROUP BY b.plan_class_id ORDER BY b.update_time desc limit 3")
    List<RecentlyPlanClassVo> queryRecentlyLearnedClass(@Param("userId") int userId);

    /**
     * 根据用户id查询该用户最近学习的课程列表
     * @param userId
     * @param sql
     * @return
     */
    @Select("select a.*,count(DISTINCT b.user_id,b.plan_class_id) as students_number,b.user_id from tb_plan_class a right join tb_plan_class_record b on a.id = b.plan_class_id " +
            "where a.is_delete = 0 group by b.plan_class_id HAVING b.user_id = ${userId} ORDER BY b.update_time desc ${sql}")
    List<PlanClass> queryRecentlyLearnedClassList(@Param("userId") int userId,@Param("sql") String sql);

    /**
     * 根据用户id查询今日学习时长
     * @param userId
     * @return
     */
    @Select("select sum(IF(difference_from_last_time=0,watch_time,difference_from_last_time)) from tb_plan_class_record where user_id = ${userId} " +
            "and FROM_UNIXTIME(update_time,\"%Y-%m-%d\") = DATE_FORMAT(NOW(),\"%Y-%m-%d\") and is_delete = 0")
    Integer queryTodayLearningTime(@Param("userId") int userId);

    /**
     * 根据用户id查询用户学习总时长
     * @param userId
     * @return
     */
    @Select("select IFNULL(sum(watch_time),0) from tb_plan_class_record where user_id = ${userId} and is_delete = 0")
    Integer queryAllLearningTime(@Param("userId") int userId);

    /**
     * 根据用户id查询今日学习课程
     * @param userId
     * @return
     */
    @Select("select a.* from tb_plan_class a INNER JOIN tb_user_plan b on a.plan_id = b.plan_id where b.user_id = ${userId} and a.is_enhance = 0 " +
            "and a.date_weight = (b.complete_schedule - case when FROM_UNIXTIME(b.update_time,\"%Y-%m-%d\") = DATE_FORMAT(NOW(),\"%Y-%m-%d\") THEN 1 ELSE 0 end)")
    PlanClassTodayVo queryTodayClass(@Param("userId") int userId);

    /**
     * 根据用户id查询第一天学习课程
     * @param userId
     * @return
     */
    @Select("select a.* from tb_plan_class a INNER JOIN tb_user_plan b on a.plan_id = b.plan_id where b.user_id = ${userId} and a.is_enhance = 0 " +
            "and a.date_weight = 1")
    PlanClassTodayVo queryTodayClassOne(@Param("userId") int userId);

    /**
     * 根据用户id和课程id查询今日学习观看视频最新记录
     * @param userId
     * @param planClassId
     * @return
     */
    @Select("select a.* from (select * from tb_plan_class_record where user_id = ${userId} and plan_class_id = ${planClassId} ORDER BY " +
            "update_time desc) a GROUP BY a.user_id")
    PlanClassRecord queryTodayClassRecord(@Param("userId") int userId,@Param("planClassId") int planClassId);

    /**
     * 根据课程id查询该课程下有多少视频
     * @param planClassId
     * @return
     */
    @Select("select count(*) from tb_class_video where plan_class_id = ${planClassId}")
    Integer queryClassVideoNum(@Param("planClassId") int planClassId);

    /**
     * 查询用户某课程观看完成的进度
     * @param userId
     * @param planClassId
     * @return
     */
    @Select("select sum(b.video_viewing_progress) from tb_plan_class a right JOIN tb_plan_class_record b on a.id = b.plan_class_id where " +
            "b.user_id = ${userId} and a.is_delete = 0 and b.plan_class_id = ${planClassId}")
    Integer queryClassViewProgress(@Param("userId")int userId, @Param("planClassId")int planClassId);

    /**
     * 查询用户计划数据
     * @param userId
     * @param planId
     * @return
     */
    @Select("select * from tb_user_plan where user_id = ${userId} and plan_id = ${planId}")
    UserPlan queryUserPlanSingRecord(@Param("userId") int userId,@Param("planId") int planId);

    /**
     * 查询计划下有多少天的课程
     * @param planId
     * @return
     */
    @Select("select IFNULL(max(date_weight),0) from tb_plan_class where plan_id = ${planId}")
    int queryMaxDateWeightByPlanId(@Param("planId") int planId);

    /**
     * 签到（修改签到记录和学习进度）
     * @param singInRecord
     * @param completeSchedule
     * @param updateTime
     * @param id
     * @return
     */
    @Update("update tb_user_plan set sing_in_record = #{singInRecord},complete_schedule = ${completeSchedule},update_time = #{updateTime} where " +
            "id = ${id}")
    int updateUserPlanSingInRecord(@Param("singInRecord") String singInRecord,@Param("completeSchedule") int completeSchedule,@Param("updateTime") String updateTime,@Param("id") int id);

    /**
     * 重新预设用户计划
     * @param userPlan
     * @return
     */
    @Update("update tb_user_plan set plan_id = ${userPlan.planId},sing_in_record = NULL,complete_schedule = 1," +
            "create_at = #{userPlan.createAt},update_time = #{userPlan.updateTime} where " +
            "user_id = ${userPlan.userId}")
    int resetUserPlan(@Param("userPlan") UserPlan userPlan);
}
