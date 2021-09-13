package com.example.babacirclecommunity.learn.dao;

import com.example.babacirclecommunity.learn.entity.Question;
import com.example.babacirclecommunity.learn.vo.QuestionTagVo;
import com.example.babacirclecommunity.learn.vo.QuestionVo;
import com.example.babacirclecommunity.personalCenter.vo.QuestionPersonalVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author JC
 * @date 2021/4/28 15:16
 */
@Component
public interface QuestionMapper {

    /**
     * 查询提问列表
     * @param content
     * @param tagId
     * @param planClassId
     * @param sql
     * @return
     */
    @Select("<script>"+
            "select a.id,a.user_id,c.user_name,c.avatar,a.title,a.description,a.plan_class_id,a.circle_id,a.type,a.cover,a.video," +
            "a.favour_num,a.comment_num,a.haplont_id,a.tags_two,b.tag_name,a.create_at " +
            "from tb_question a LEFT JOIN tb_tags b on a.tags_two = b.id left join tb_user c on a.user_id = c.id where " +
            "<if test='tagId != null and tagId != 125'> a.tags_two = ${tagId} and </if>"+
            "<if test='planClassId != null'> a.plan_class_id = ${planClassId} and </if>"+
            "<if test='content != null'>a.description LIKE CONCAT('%',#{content},'%') and </if>"+
            "a.is_delete = 0 ${sql}"+
            "</script>")
    List<QuestionTagVo> queryQuestionList(@Param("content") String content,@Param("tagId") Integer tagId,@Param("planClassId") Integer planClassId,@Param("sql") String sql);

    /**
     * 根据userId查询提问列表
     * @param uId
     * @param paging
     * @return
     */
    @Select("select a.id,a.title,a.description,a.content_type,a.cover_img,a.favour,a.collect,a.comment,a.tags_two,b.tag_name from " +
            "tb_question a LEFT JOIN tb_tags b on a.tags_two = b.id where a.u_id = ${uId} and a.is_delete = 1 ORDER BY a.create_at desc ${paging}")
    List<QuestionVo> queryQuestionListByUser(@Param("uId") int uId, @Param("paging") String paging);

    /**
     * 根据tagId标签查询提问列表
     * @param sql
     * @param tagId
     * @return
     */
    @Select("select a.id,a.title,a.description,a.content_type,a.cover_img,a.favour,a.collect,a.comment,a.tags_two,b.tag_name from " +
            "tb_question a LEFT JOIN tb_tags b on a.tags_two = b.id where a.tags_two = ${tagId} and a.is_delete = 1 ${sql}")
    List<QuestionVo> queryQuestionListByTag(@Param("tagId") int tagId,@Param("sql") String sql);

    /**
     * 增加提问帖
     * @param question
     * @return
     */
    @Insert("insert into tb_question(user_id,title,description,tags_two,haplont_id,plan_class_id,circle_id,type,cover,video,create_at) " +
            "value(${question.userId},#{question.title},#{question.description},${question.tagsTwo},${question.haplontId},${question.planClassId}," +
            "${question.circleId},${question.type},#{question.cover},#{question.video},#{question.createAt})")
    @Options(useGeneratedKeys=true, keyProperty="question.id",keyColumn="id")
    int addQuestion(@Param("question")Question question);

    /**
     * 根据id查询提问详情
     * @param id
     * @return
     */
    @Select("select a.id,a.user_id,b.user_name,b.avatar,a.title,a.description,a.circle_id,a.type,a.cover,a.video,a.favour_num,a.comment_num," +
            "c.community_name,a.create_at from tb_question a left join tb_user b on a.user_id = b.id left join tb_community c " +
            "on a.tags_two = c.tag_id where a.id = ${id} and a.is_delete = 0")
    QuestionVo queryQuestionById(@Param("id") int id);

    /**
     * 海报
     * @param id
     * @return
     */
    @Select("select a.title,a.cover_img,a.anonymous,b.user_name as uName,b.avatar from tb_question a inner join tb_user b on a.u_id=b.id where a.id = #{id} and a.is_delete = 1")
    QuestionTagVo queryQuestionPosters(@Param("id") String id);

    /**
     * 修改帖子点赞数
     * @param id
     * @param math
     * @return
     */
    @Update("update tb_question set favour_num = favour_num ${math} 1 where id = ${id}")
    int updateQuestionGive(@Param("id") int id,@Param("math") String math);

    /**
     * 修改帖子收藏数
     * @param id
     * @param math
     * @return
     */
    @Update("update tb_question set collect = collect ${math} 1 where id = ${id}")
    int updateQuestionCollect(@Param("id") int id,@Param("math") String math);

    /**
     * 修改帖子评论数
     * @param id
     * @param math
     * @return
     */
    @Update("update tb_question set comment_num = comment_num ${math} 1 where id = ${id}")
    int updateQuestionComment(@Param("id") int id,@Param("math") String math);

    /**
     * 根据用户id查询我的回答列表
     * @param userId
     * @param sql
     * @return
     */
    @Select("select a.comment_content,a.create_at,a.t_id,b.title,b.description,b.user_id,c.user_name,c.avatar,b.is_delete," +
            "b.favour_num,b.comment_num from tb_learn_comment a left join tb_question b on a.t_id = b.id left join tb_user c on b.user_id = c.id " +
            "where a.p_id = ${userId} and a.t_type = 0 and a.is_delete = 1 order by a.create_at desc ${sql}")
    List<QuestionPersonalVo> queryMyAskList(@Param("userId") int userId,@Param("sql") String sql);
}
