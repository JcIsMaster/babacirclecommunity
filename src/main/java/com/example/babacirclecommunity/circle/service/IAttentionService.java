package com.example.babacirclecommunity.circle.service;


import com.example.babacirclecommunity.circle.entity.Attention;
import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.circle.vo.GuessYouLike;
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
     * @param paging
     * @return
     */
    List<CircleClassificationVo> queryPostsPeopleFollow(int userId, Paging paging);

    /**
     * 关注首页猜你喜欢
     * @param userId
     * @return
     */
    List<GuessYouLike> guessYouLike(int userId);

}
