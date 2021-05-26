package com.example.babacirclecommunity.learn.dao;

import com.example.babacirclecommunity.learn.entity.Question;
import com.example.babacirclecommunity.learn.vo.QuestionTagVo;
import com.example.babacirclecommunity.learn.vo.QuestionVo;
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
     * @param sql
     * @return
     */
    @Select("select a.id,a.title,a.description,a.content_type,a.cover_img,a.favour,a.collect,a.comment,a.tags_two,b.tag_name from " +
            "tb_question a LEFT JOIN tb_tags b on a.tags_two = b.id where a.is_delete = 1 ${sql}")
    List<QuestionVo> queryQuestionList(@Param("sql") String sql);

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
    @Insert("insert into tb_question(u_id,title,tags_one,tags_two,description,anonymous,content_type,cover_img,video,create_at) value(${question.uId},#{question.title},${question.tagsOne},${question.tagsTwo},#{question.description},${question.anonymous},${question.contentType},#{question.coverImg},#{question.video},#{question.createAt})")
    @Options(useGeneratedKeys=true, keyProperty="question.id",keyColumn="id")
    int addQuestion(@Param("question")Question question);

    /**
     * 根据id查询提问详情
     * @param id
     * @return
     */
    @Select("select a.*,b.tag_name,COALESCE(SUM(c.gold_num),0) as goldNum from tb_question a INNER JOIN tb_tags b " +
            "on a.tags_two = b.id LEFT JOIN tb_learn_post_exceptional c on a.id = c.t_id and c.type = 0 " +
            "where a.id = ${id} and a.is_delete = 1")
    QuestionTagVo queryQuestionById(@Param("id") int id);

    /**
     * 修改帖子点赞数
     * @param id
     * @param math
     * @return
     */
    @Update("update tb_question set favour = favour ${math} 1 where id = ${id}")
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
    @Update("update tb_question set comment = comment ${math} 1 where id = ${id}")
    int updateQuestionComment(@Param("id") int id,@Param("math") String math);
}
