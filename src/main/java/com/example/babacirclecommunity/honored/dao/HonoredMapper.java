package com.example.babacirclecommunity.honored.dao;

import com.example.babacirclecommunity.honored.entity.Honored;
import com.example.babacirclecommunity.honored.entity.HonoredPoints;
import com.example.babacirclecommunity.talents.entity.Talents;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * @author JC
 * @date 2021/11/10 13:39
 */
@Component
public interface HonoredMapper {

    /**
     * 查询用户荣誉等级信息
     * @param userId
     * @return
     */
    @Select("select * from tb_honored where user_id = ${userId}")
    Honored queryUserHonored(@Param("userId") int userId);

    /**
     * 添加用户荣誉等级信息
     * @param userId
     * @return
     */
    @Insert("insert into tb_honored(user_id) values(${userId})")
    int addHonored(@Param("userId") int userId);

    /**
     * 添加用户荣誉积分记录
     * @param honoredPoints
     * @return
     */
    @Update("insert into tb_honored_points(user_id,points,type,source,create_at) values(${honoredPoints.userId},${honoredPoints.points}," +
            "${honoredPoints.type},${honoredPoints.source},#{honoredPoints.createAt})")
    int addHonoredPoints(@Param("honoredPoints") HonoredPoints honoredPoints);

    /**
     * 根据id查询匹配信息
     * @param userId
     * @return
     */
    @Select("select count(*) from tb_parameter where user_id = ${userId}")
    int queryParameterById(@Param("userId") int userId);

    /**
     * 根据今日用户id查询圈子文章数量
     * @param userId 用户id
     * @return
     */
    @Select("select count(id) from tb_circles where user_id = ${userId} and " +
            "FROM_UNIXTIME(create_at,\"%Y-%m-%d\") = DATE_FORMAT(NOW(),\"%Y-%m-%d\")")
    int queryTodayCircleCount(@Param("userId") int userId);

    /**
     * 根据id查询人才名片
     * @param userId
     * @return
     */
    @Select("select count(*) from tb_talents where user_id = ${userId} and is_delete = 0")
    int queryTalentById(@Param("userId") int userId);

    /**
     * 查询用户今日发布货源/合作帖子数量
     * @param userId 用户id
     * @param tagId 分类id 12货源 13合作
     * @return
     */
    @Select("select count(*) from tb_resources where u_id = ${userId} and tags_one = ${tagId} and " +
            "FROM_UNIXTIME(create_at,\"%Y-%m-%d\") = DATE_FORMAT(NOW(),\"%Y-%m-%d\")")
    int queryTodayPostedPostsCount(@Param("userId") int userId,@Param("tagId") int tagId);

}
