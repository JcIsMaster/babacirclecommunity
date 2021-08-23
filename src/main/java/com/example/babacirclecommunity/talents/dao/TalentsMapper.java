package com.example.babacirclecommunity.talents.dao;

import com.example.babacirclecommunity.talents.entity.Talents;
import com.example.babacirclecommunity.talents.vo.TalentsPersonalVo;
import com.example.babacirclecommunity.talents.vo.TalentsVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author JC
 * @date 2021/6/4 14:11
 */
@Component
public interface TalentsMapper {

    /**
     * 查询人才列表
     * @param content
     * @param city
     * @param sql
     * @return
     */
    @Select("<script>"+
            "select a.id,a.user_id,a.position,a.introduction,b.user_name,b.user_sex as sex,b.avatar,b.curr_province,b.city from tb_talents a " +
            "inner join tb_user b on a.user_id = b.id where " +
            "<if test='city != null'> b.city LIKE CONCAT('%',#{city},'%') and </if>"+
            "<if test='content != null'>b.user_name LIKE CONCAT('%',#{content},'%') or a.position LIKE CONCAT('%',#{content},'%') and </if>"+
            "a.is_delete = 0 and b.is_delete = 1 ${sql}" +
            "</script>")
    List<TalentsVo> queryTalentsList(@Param("content") String content, @Param("city") String city, @Param("sql") String sql);

    /**
     * 根据user_id查询人才名片
     * @param otherId
     * @return
     */
    @Select("select a.id,b.id as user_id,b.avatar,b.user_name,b.curr_province,b.city," +
            "a.introduction,a.position,a.specialty,a.img_works,a.video_works from tb_talents a right join tb_user b on a.user_id = b.id " +
            "where b.id = ${otherId} and b.is_delete = 1")
    TalentsPersonalVo queryTalentByUserId(@Param("otherId") int otherId);

    /**
     * 根据id查询人才名片
     * @param userId
     * @return
     */
    @Select("select * from tb_talents where user_id = ${userId} and is_delete = 0")
    Talents queryTalentById(@Param("userId") int userId);

    /**
     * 修改个人名片
     * @param talents
     * @return
     */
    @Update("update tb_talents set position = #{talents.position},specialty = #{talents.specialty},introduction = #{talents.introduction}," +
            "img_works = #{talents.imgWorks},video_works = #{talents.videoWorks} where id = ${talents.id}")
    int updatePersonalTalent(@Param("talents") Talents talents);

    /**
     * 新增个人名片
     * @param talents
     * @return
     */
    @Insert("insert into tb_talents(user_id,position,specialty,introduction,img_works,video_works,create_at) " +
            "values(${talents.userId},#{talents.position},#{talents.specialty},#{talents.introduction},#{talents.imgWorks}," +
            "#{talents.videoWorks},#{talents.createAt})")
    @Options(useGeneratedKeys=true, keyProperty="talents.id",keyColumn="id")
    int addPersonalTalent(@Param("talents") Talents talents);
}
