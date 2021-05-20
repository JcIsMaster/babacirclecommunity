package com.example.babacirclecommunity.CircleFriends.dao;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author MQ
 * @date 2021/5/20 13:37
 */
@Component
public interface CircleFriendsMapper {

    /**
     * 查询圈子海报图
     * @return
     */
    @Select("select img_url from tb_circle_poster")
    String[] queryCirclePoster();
}
