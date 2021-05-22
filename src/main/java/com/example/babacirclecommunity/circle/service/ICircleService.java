package com.example.babacirclecommunity.circle.service;

import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.common.utils.Paging;

import java.text.ParseException;
import java.util.List;

/**
 * @author MQ
 * @date 2021/5/20 19:39
 */
public interface ICircleService {

    /**
     * 查询我关注的人发的帖子
     * @param userId
     * @param paging
     * @return
     */
    List<CircleClassificationVo> queryPostsPeopleFollow(int userId, Paging paging);

    /**
     * 查询视频或者图片
     * @param type 0图片 ，1视频
     * @param paging
     * @param userId
     * @return
     */
    List<CircleClassificationVo> queryImagesOrVideos(int type,Paging paging,int userId);

    /**
     * 查询单个圈子的帖子
     * @param id 圈子id
     * @param userId 用户id
     * @return
     * @throws ParseException
     */
    CircleClassificationVo querySingleCircle(int id,int userId) throws ParseException;


}
