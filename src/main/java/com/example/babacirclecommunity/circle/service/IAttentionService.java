package com.example.babacirclecommunity.circle.service;


import com.example.babacirclecommunity.circle.entity.Attention;
import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.common.utils.Paging;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/6 13:26
 */
public interface IAttentionService {

    /**
     * 添加关注信息
     * @param attention
     * @return
     */
    int addAttention(Attention attention);

    /**
     * 查询我关注人的发的帖子
     * @param userId
     * @return
     */
    List<CircleClassificationVo> queryPostsPeopleFollow(int userId, Paging paging);


}
