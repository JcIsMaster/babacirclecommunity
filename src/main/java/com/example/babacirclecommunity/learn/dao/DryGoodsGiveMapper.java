package com.example.babacirclecommunity.learn.dao;

import com.example.babacirclecommunity.learn.entity.Give;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * @author JC
 * @date 2021/4/19 16:38
 */
@Component
public interface DryGoodsGiveMapper {

    /**
     *查询我是否点赞过该帖子
     * @param learnType 帖子类型
     * @param userId 用户id
     * @param tid 帖子id
     * @return
     */
    @Select("select COALESCE(count(*)) from tb_learn_give where learn_type=${learnType} and u_id=${userId} and zq_id=${tid} and give_cancel=1")
    Integer whetherGive(@Param("learnType") int learnType, @Param("userId") int userId, @Param("tid") int tid);

    /**
     * 查询数据库是否存在该条数据
     * @param learnType 帖子类型
     * @param userId 用户id
     * @param tid 帖子id
     * @return
     */
    @Select("select * from tb_learn_give where learn_type=${learnType} and u_id=${userId} and zq_id=${tid}")
    Give selectCountWhether(@Param("learnType") int learnType, @Param("userId") int userId, @Param("tid") int tid);

    /**
     *增加点赞信息
     *@param learnType 帖子类型
     *@param id 帖子id
     *@param userId 用户id
     *@param createAt 创建时间
     * @return
     */
    @Insert("insert into tb_learn_give(learn_type,zq_id,u_id,create_at,give_cancel)values(${learnType},${id},${userId},#{createAt},1)")
    int giveLike(@Param("learnType") int learnType, @Param("id")int id,@Param("userId") int userId, @Param("createAt") String createAt);

    /**
     * 修改点赞状态
     * @param id 点赞id
     * @param status 状态id
     * @return
     */
    @Update("update tb_learn_give set give_cancel=${status} where id=${id}")
    int updateGiveStatus(@Param("id") int id,@Param("status") int status);

    /**
     * 得到帖子点赞数量
     * @param learnType 帖子类型
     * @param tid 帖子id
     * @return
     */
    @Select("select COALESCE(count(*)) from tb_learn_give where learn_type=${learnType} and zq_id=${tid} and give_cancel=1")
    Integer selectGiveNumber(@Param("learnType") int learnType, @Param("tid") int tid);
}
