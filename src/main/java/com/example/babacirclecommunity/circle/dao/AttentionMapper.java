package com.example.babacirclecommunity.circle.dao;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;


/**
 * @author MQ
 * @date 2021/3/6 13:33
 */
@Component
public interface AttentionMapper {

    /**
     * 查询我是否关注该用户
     * @param guId 关注人id
     * @param bgId 被关注人id
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_user_attention where gu_id=${guId} and bg_id=${bgId} and is_delete=1")
    int queryWhetherAttention(@Param("guId") int guId,@Param("bgId") int bgId);

}
