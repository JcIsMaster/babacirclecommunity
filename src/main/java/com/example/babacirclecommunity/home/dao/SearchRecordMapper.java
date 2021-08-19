package com.example.babacirclecommunity.home.dao;

import com.example.babacirclecommunity.home.entity.SearchHistory;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author JC
 * @date 2021/5/20 14:11
 */
@Component
public interface SearchRecordMapper {

    /**
     * 增加搜索历史记录
     * @param historicalContent
     * @param createAt
     * @return
     */
    @Insert("insert into tb_search_history(historical_content,create_at,user_id)values(#{historicalContent},#{createAt},${userId})")
    int addSearchRecord(@Param("historicalContent") String historicalContent, @Param("createAt") String createAt, @Param("userId") int userId);

    /**
     * 根据用户id查询历史记录
     * @param userId
     * @return
     */
    @Select("select id,historical_content from tb_search_history where user_id = ${userId} and is_delete = 1 order by create_at desc limit 8")
    List<SearchHistory> selectSearchRecordByUserId(@Param("userId") int userId);

    /**
     * 查询热搜记录top5
     * @return
     */
    @Select("select historical_content from tb_search_history GROUP BY historical_content ORDER BY count(*) desc limit 5")
    List<String> queryHotSearchHistorical();

    /**
     * 删除搜索历史记录
     * @param userId
     * @return
     */
    @Update("update tb_search_history set is_delete = 0 where user_id = ${userId}")
    int deleteHistorySearch(@Param("userId") int userId);
}
