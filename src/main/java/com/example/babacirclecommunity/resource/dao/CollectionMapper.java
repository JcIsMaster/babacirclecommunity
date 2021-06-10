package com.example.babacirclecommunity.resource.dao;

import com.example.babacirclecommunity.resource.entity.Collection;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * @author MQ
 * @date 2021/3/2 17:34
 */
@Component
public interface CollectionMapper {

    /**
     * 根据用户id和帖子id查询数据是否存在数据库
     * @param userId 用户id
     * @param tid 帖子id
     * @return
     */
    @Select("select * from tb_user_collection where u_id=${userId} and t_id=${tid} and type_collection=${typeCollection}")
    Collection selectCountWhether(@Param("userId") int userId, @Param("tid") int tid,@Param("typeCollection") int typeCollection);

    /**
     * 根据用户id和帖子id查看是否收藏着帖子
     * @param userId 用户id
     * @param tid 帖子id
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_user_collection where u_id=${userId} and t_id=${tid} and is_delete=1")
    int selectWhetherCollection(@Param("userId") int userId,@Param("tid") int tid);

    /**
     * 添加收藏信息
     * @param uId 用户id
     * @param tId 贴子id
     * @param createAt 创建时间
     * @param remarks 备注
     * @return
     */
    @Insert("insert into tb_user_collection(u_id,t_id,create_at,remarks,type_collection) values(${uId},${tId},#{createAt},#{remarks},${typeCollection})")
    int addCollectionPost(@Param("uId") int uId,@Param("tId") int tId,@Param("createAt") String createAt,@Param("remarks") String remarks,@Param("typeCollection") int typeCollection);

    /**
     * 修改帖子收藏的状态
     * @param id 收藏id
     * @param status 状态id
     * @return
     */
    @Update("update tb_user_collection set is_delete=${status} where id=${id} and type_collection=${typeCollection}")
    int updateCollectionStatus(@Param("id") int id,@Param("status") int status,@Param("typeCollection") int typeCollection);

    /**
     * 得到帖子的收藏数量
     * @param id 帖子id
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_user_collection where t_id=${id} and is_delete=1")
    int selectCollectNumber(@Param("id") int id);
}
