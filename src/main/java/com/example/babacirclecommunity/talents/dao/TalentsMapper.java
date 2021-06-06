package com.example.babacirclecommunity.talents.dao;

import com.example.babacirclecommunity.talents.entity.Talents;
import com.example.babacirclecommunity.talents.vo.TalentsVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
            "select id,avatar,nick_name,sex,city,tag_primary,tag_individuality_one,tag_individuality_two,introduction from tb_talents where " +
            "<if test='city != null'> city = #{city} and </if>"+
            "<if test='content != null'>nick_name LIKE CONCAT('%',#{content},'%') or introduction LIKE CONCAT('%',#{content},'%') and </if>"+
            "is_delete = 1 ${sql}" +
            "</script>")
    List<Talents> queryTalentsList(@Param("content") String content, @Param("city") String city, @Param("sql") String sql);

    /**
     * 根据id查询人才名片
     * @param userId
     * @return
     */
    @Select("select a.id,a.avatar,a.nick_name,IFNULL(b.user_sex,1) as sex,a.city,a.tag_primary,a.tag_individuality_one,a.tag_individuality_two," +
            "a.introduction,b.picture,b.introduce from tb_talents a inner join tb_user b on a.id = b.id " +
            "where a.id = ${userId} and a.is_delete = 1")
    TalentsVo queryTalentById(@Param("userId") int userId);

    /**
     * 修改个人名片
     * @param talents
     * @return
     */
    @Update("update tb_talents set city = #{talents.city},tag_primary = #{talents.tagPrimary},tag_individuality_one = #{talents.tagIndividualityOne}," +
            "tag_individuality_two = #{talents.tagIndividualityTwo},introduction = #{talents.introduction} where id = ${talents.id}")
    int updatePersonalTalent(@Param("talents") Talents talents);

    /**
     * 新增个人名片
     * @param talents
     * @return
     */
    @Insert("insert into tb_talents(id,avatar,nick_name,sex,city,tag_primary,tag_individuality_one,tag_individuality_two,introduction,create_at) " +
            "values(${talents.id},#{talents.avatar},#{talents.nickName},${talents.sex},#{talents.city},#{talents.tagPrimary},#{tagPrimary.tagIndividualityOne}," +
            "#{talents.tagIndividualityTwo},#{talents.introduction},#{talents.createAt})")
    int addPersonalTalent(@Param("talents") Talents talents);
}
