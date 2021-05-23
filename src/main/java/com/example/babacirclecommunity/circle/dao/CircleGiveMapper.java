package com.example.babacirclecommunity.circle.dao;

import com.example.babacirclecommunity.circle.entity.Give;
import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/2/27 16:04
 */
@Component
public interface CircleGiveMapper {

    /**
     * 圈子点赞表
     * 查看帖子点过赞的头像
     * @param zqId
     * @return
     */
    @Select("select b.avatar as giveAvatar from tb_circles_give a INNER JOIN tb_user b on a.u_id=b.id where a.zq_id=${zqId} and a.give_cancel=1 order by a.create_at limit 8")
    String[] selectCirclesGivePersonAvatar(@Param("zqId") int zqId);

    /**
     * 得到帖子点赞数量
     * @param tid 帖子id
     * @return
     */
    @Select("select COALESCE(count(*)) from tb_circles_give where zq_id=${tid} and give_cancel=1")
    Integer selectGiveNumber(@Param("tid") int tid);

    /**
     * 查询我是否点赞过这帖子
     * @param userId 用户id
     * @param tid 帖子id
     * @return
     */
    @Select("select COALESCE(count(*)) from tb_circles_give where u_id=${userId} and zq_id=${tid} and give_cancel=1")
    Integer whetherGive(@Param("userId") int userId,@Param("tid") int tid);

    /**
     * 查询我点赞过的圈子帖子
     * @param userId 用户id
     * @param paging
     * @return
     */
    @Select("select b.id,b.content,b.cover,b.video,b.browse,b.create_at,c.tag_name,c.id,d.id as uId,d.user_name,d.avatar from tb_circles_give a INNER JOIN tb_circles b on a.zq_id=b.id " +
            "INNER JOIN tb_tags c on b.tags_two=c.id INNER JOIN tb_user d on b.u_id=d.id " +
            "where a.u_id=${userId} and b.is_delete=1 ${paging}")
    List<CircleClassificationVo> queryGiveCircle(@Param("userId") int userId,String paging);

    /**
     * 统计我点赞过的圈子帖子数量
     * @param userId 用户id
     * @return
     */
    @Select("select COALESCE(count(b.id),0) from tb_circles_give a INNER JOIN tb_circles b on a.zq_id=b.id " +
            "INNER JOIN tb_tags c on b.tags_two=c.id INNER JOIN tb_user d on b.u_id=d.id " +
            "where a.u_id=${userId} and b.is_delete=1")
    int countGiveCircle(@Param("userId") int userId);

    /**
     * 查询数据库是否存在该条数据
     * @param userId 用户id
     * @param tid 帖子id
     * @return
     */
    @Select("select * from tb_circles_give where u_id=${userId} and zq_id=${tid}")
    Give selectCountWhether(@Param("userId") int userId, @Param("tid") int tid);

    /**
     **增加点赞信息
     *@param id 帖子id
     *@param userId 用户id
     *@param createAt 创建时间
     * @return
     */
    @Insert("insert into tb_circles_give(zq_id,u_id,create_at,give_cancel)values(${id},${userId},#{createAt},1)")
    int givePost(@Param("id")int id,@Param("userId") int userId,@Param("createAt") String createAt);

    /**
     * 修改点赞状态
     * @param id 点赞id
     * @param status 状态id
     * @return
     */
    @Update("update tb_circles_give set give_cancel=${status} where id=${id}")
    int updateGiveStatus(@Param("id") int id,@Param("status") int status);

}
