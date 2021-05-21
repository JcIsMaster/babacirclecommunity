package com.example.babacirclecommunity.circle.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/2/27 15:37
 */
@Component
public interface CommentMapper {


    /**
     * 查看帖子的评论数量
     * @param tid
     * @return
     */
    @Select("select COALESCE(count(*)) from tb_comment where t_id=${tid}")
    Integer selectCommentNumber(@Param("tid") int tid);
}
