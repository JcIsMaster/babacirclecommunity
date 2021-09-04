package com.example.babacirclecommunity.circle.service;

import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.common.utils.Paging;

import java.util.List;

/**
 * @author MQ
 * @date 2021/5/22 14:59
 */
public interface ICircleGiveService {

    /**
     * 查询我点过赞的圈子帖子
     * @param userId 当前用户id
     * @param otherId 他人用户id
     * @param paging
     * @return
     */
    List<CircleClassificationVo> queryGiveCircle(int userId, int otherId, Paging paging);

    /**
     * 点赞
     * @param id 帖子id
     * @param userId 用户id
     * @param thumbUpId
     * @return
     */
    int givePost(int id,int userId,int thumbUpId);
}
