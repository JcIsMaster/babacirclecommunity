package com.example.babacirclecommunity.circle.dao;


import com.example.babacirclecommunity.circle.entity.Browse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author MQ
 * @create 2021/2/22
 **/
@Component
public interface BrowseMapper {
    /**
     * 增加浏览量
     * @param browse
     * @return
     */
    @Insert("insert into tb_browse(u_id,zq_id,type,create_at)values(${browse.uId},${browse.zqId},${browse.type},${browse.createAt})")
    int addBrowse(@Param("browse") Browse browse);

    /**
     * 查询上次观看帖子的时间
     * @param tid
     * @param userId
     * @return
     */
    @Select("select create_at from tb_browse where zq_id=${tid} and u_id=${userId} and type=1 and is_delete=1 order by create_at desc limit 1")
    String selectCreateAt(@Param("tid") int tid,@Param("userId") int userId);


}
