package com.example.babacirclecommunity.resourceMatch.dao;

import com.example.babacirclecommunity.resourceMatch.entity.Parameter;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Administrator
 */
@Mapper
@Component
public interface MatchUserDao {


    /**
     * 根据id查询
     * @param userId
     * @return
     */
    @Select("SELECT *,ROUND(DATEDIFF(CURDATE(), birthday)/365.2422) as age,user_id from tb_parameter as a inner JOIN tb_user as b on a.user_id " +
            "=b.id WHERE a.user_id=${userId}")
    Parameter ParameterById (@Param("userId") int userId);

    /**
     * 根据用户id修改
     * @param userId
     * @param text
     * @return
     */
    @Update("UPDATE tb_parameter set json_class=#{text},create_at=unix_timestamp() WHERE user_id=${userId}")
    int updateParameter(@Param("userId") int userId,@Param("text") String text);

    /**
     * 模糊查询符合要求的用户
     * @param value
     * @param userId
     * @param sql
     * @param str
     * @return
     */
    @Select("SELECT user_name,user_sex,avatar,ROUND(DATEDIFF(CURDATE(), birthday)/365.2422) as age,a.json_class,user_id " +
            "from tb_parameter as a inner JOIN tb_user as b on a.user_id =b.id  " +
            "WHERE json_class like #{value} ${sql} and user_id!=${userId} ${str} limit 0 , 10")
    List<Parameter> Parameter (@Param("value") String value,@Param("userId") int userId,@Param("sql") String sql,@Param("str") String str);

    /**
     * 模糊查询符合要求的用户人数
     * @param value
     * @param userId
     * @param sql
     * @param str
     * @return
     */
    @Select("SELECT count(*) as count " +
            "from tb_parameter as a inner JOIN tb_user as b on a.user_id =b.id  " +
            "WHERE json_class like #{value} ${sql} and user_id!=${userId} ${str} limit 0 , 10")
    int count(@Param("value") String value,@Param("userId") int userId,@Param("sql") String sql,@Param("str") String str);

    /**
     * 新增
     * @param userId
     * @param text
     * @return
     */
    @Insert("INSERT into tb_parameter(user_id,json_class,create_at) VALUES(${userId},#{text},unix_timestamp())")
    int insertParameter(@Param("userId") int userId,@Param("text") String text);
}
