package com.example.babacirclecommunity.plan.dao;

import com.example.babacirclecommunity.plan.entity.*;
import com.example.babacirclecommunity.plan.vo.PlanClassVo;
import com.example.babacirclecommunity.plan.vo.TopicOptionsVo;
import com.example.babacirclecommunity.plan.vo.UserPlanVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
    @Insert("insert into tb_user_plan(user_id,plan_id,create_at) value(${userPlan.userId},${userPlan.planId},#{userPlan.createAt})")
    @Options(useGeneratedKeys=true, keyProperty="userPlan.id",keyColumn="id")
    int generatePlan(@Param("userPlan") UserPlan userPlan);

    /**
     * 查看用户计划
     * @param userId
     * @return
     */
    @Select("select id,user_id,plan_id,sing_in_record,create_at from tb_user_plan where user_id = ${userId}")
    UserPlanVo queryUserPlan(@Param("userId") int userId);

    /**
     * 根据目标参数查找计划
     * @param planOptions
     * @return
     */
    @Select("select * from tb_plan where options_array = #{planOptions} and is_delete = 0")
    Plan queryPlanByOptions(@Param("planOptions") String planOptions);

    /**
     * 查询（生成计划页）课程学习列表
     * @param planId
     * @param surplus
     * @return
     */
    @Select("select * from tb_plan_class where plan_id = ${planId} and is_delete = 0 and date_weight > ${surplus} order by date_weight limit 3")
    List<PlanClass> queryClassLearnList(@Param("planId") int planId, @Param("surplus") int surplus);

    /**
     * 查询计划课程详情
     * @param planClassId 计划课程id
     * @return
     */
    @Select("select * from tb_plan_class where id = ${planClassId} and is_delete = 0")
    PlanClassVo queryPlanClassDetail(@Param("planClassId") int planClassId);

    /**
     * 根据课程id查询该课程下的所有视频
     * @param planClassId
     * @return
     */
    @Select("select a.video_id,b.video_name,b.video_desc,b.video_cover,b.video_address from tb_class_video a inner join tb_video b " +
            "on a.video_id = b.id where a.plan_class_id = ${planClassId} and b.is_delete = 0")
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
    @Select("select * from tb_plan_class where is_enhance = 1 and is_delete = 0 ${sql}")
    List<PlanClass> queryEnhancePlanListByTag(@Param("tagId") int tagId,@Param("sql") String sql);
}
