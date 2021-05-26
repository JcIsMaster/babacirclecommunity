package com.example.babacirclecommunity.learn.dao;

import com.example.babacirclecommunity.learn.entity.Collect;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * @author JC
 * @date 2021/4/20 16:07
 */
@Component
public interface DryGoodsCollectMapper {

    /**
     *查询我是否收藏过该帖子
     * @param learnType 帖子类型
     * @param userId 用户id
     * @param tid 帖子id
     * @return
     */
    @Select("select COALESCE(count(*)) from tb_learn_collect where learn_type=${learnType} and u_id=${userId} and zq_id=${tid} and give_cancel=1")
    Integer whetherCollect(@Param("learnType") int learnType, @Param("userId") int userId, @Param("tid") int tid);

    /**
     * 查询数据库是否存在该条数据
     * @param learnType 帖子类型
     * @param userId 用户id
     * @param tid 帖子id
     * @return
     */
    @Select("select * from tb_learn_collect where learn_type=${learnType} and u_id=${userId} and zq_id=${tid}")
    Collect selectCountWhether(@Param("learnType") int learnType, @Param("userId") int userId, @Param("tid") int tid);

    /**
     *增加收藏信息
     *@param learnType 帖子类型
     *@param id 帖子id
     *@param userId 用户id
     *@param createAt 创建时间
     * @return
     */
    @Insert("insert into tb_learn_collect(learn_type,zq_id,u_id,create_at,give_cancel)values(${learnType},${id},${userId},#{createAt},1)")
    int giveCollect(@Param("learnType") int learnType, @Param("id")int id,@Param("userId") int userId, @Param("createAt") String createAt);

    /**
     * 修改收藏状态
     * @param id 点赞id
     * @param status 状态id
     * @return
     */
    @Update("update tb_learn_collect set give_cancel=${status} where id=${id}")
    int updateCollectStatus(@Param("id") int id,@Param("status") int status);

    /**
     * 得到帖子收藏数量
     * @param learnType 帖子类型
     * @param tid 帖子id
     * @return
     */
    @Select("select COALESCE(count(*)) from tb_learn_collect where learn_type=${learnType} and zq_id=${tid} and give_cancel=1")
    Integer selectCollectNumber(@Param("learnType") int learnType, @Param("tid") int tid);
}
